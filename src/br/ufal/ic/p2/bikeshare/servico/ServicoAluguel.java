package br.ufal.ic.p2.bikeshare.servico;

import br.ufal.ic.p2.bikeshare.dominio.Aluguel;
import br.ufal.ic.p2.bikeshare.dominio.Bicicleta;
import br.ufal.ic.p2.bikeshare.dominio.ConfiguracaoSistema;
import br.ufal.ic.p2.bikeshare.dominio.Estacao;
import br.ufal.ic.p2.bikeshare.dominio.FormatoDataHora;
import br.ufal.ic.p2.bikeshare.dominio.Usuario;
import br.ufal.ic.p2.bikeshare.excecao.aluguel.AluguelException;
import br.ufal.ic.p2.bikeshare.excecao.bicicleta.BicicletaException;
import br.ufal.ic.p2.bikeshare.excecao.estacao.EstacaoException;
import br.ufal.ic.p2.bikeshare.excecao.usuario.UsuarioException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DateTimeException;
import java.time.LocalDateTime;

import static br.ufal.ic.p2.bikeshare.servico.ValidadorEntrada.emBranco;


final class ServicoAluguel {
    private final EstadoSistema estado;

    ServicoAluguel(EstadoSistema estado) {
        this.estado = estado;
    }

    String retirarBicicleta(String idUsuario, String idEstacao, String idBicicleta, String data) {
        Usuario usuario = estado.buscarUsuario(idUsuario);
        if (usuario.estaInadimplente()) {
            throw new UsuarioException("Usuario inadimplente.");
        }
        Estacao estacao = estado.buscarEstacao(idEstacao);
        Bicicleta bicicleta = estado.buscarBicicleta(idBicicleta);
        LocalDateTime inicio = validarData(data);
        validarRetirada(usuario, bicicleta, idEstacao);

        Aluguel aluguel = new Aluguel(estado.gerarIdAluguel(), idUsuario, idBicicleta, idEstacao, inicio);
        estado.atualizar(aluguel);
        estado.atualizar(estacao.removerBicicleta());
        estado.atualizar(bicicleta.retirarDaEstacao());
        estado.atualizar(usuario.comAluguelAtivo(aluguel.getId()));
        return aluguel.getId();
    }

    void devolverBicicleta(String idAluguel, String idEstacao, String data) {
        Aluguel aluguel = estado.buscarAluguel(idAluguel);
        Estacao destino = estado.buscarEstacao(idEstacao);
        LocalDateTime fim = validarData(data);
        validarDevolucao(aluguel, destino, fim);

        long minutos = aluguel.minutosAte(fim);
        if (minutos > ConfiguracaoSistema.getMinutosMaximosAluguel()) {
            registrarExpiracao(aluguel, idEstacao, fim);
        } else {
            registrarDevolucao(aluguel, idEstacao, fim, minutos);
        }
        estado.atualizar(destino.adicionarBicicleta());
    }

    String getAtributo(String id, String atributo) {
        return estado.buscarAluguel(id).getAtributo(atributo);
    }

    private void validarRetirada(Usuario usuario, Bicicleta bicicleta, String idEstacao) {
        if (usuario.possuiAluguelAtivo()) {
            throw new UsuarioException("Usuario ja possui um aluguel em andamento.");
        }
        if (!bicicleta.estaNaEstacao(idEstacao)) {
            throw new BicicletaException("Bicicleta nao esta nesta estacao.");
        }
        bicicleta.validarRetirada();
        usuario.validarPagamentoPendente();
    }

    private void validarDevolucao(Aluguel aluguel, Estacao destino, LocalDateTime fim) {
        if (!aluguel.estaEmAndamento()) {
            throw new AluguelException("Aluguel nao esta em andamento.");
        }
        if (fim.isBefore(aluguel.getInicio())) {
            throw new AluguelException("Data de devolucao anterior a data de retirada.");
        }
        if (!destino.possuiVagaDisponivel()) {
            throw new EstacaoException("Estacao cheia.");
        }
    }

    
    private void registrarExpiracao(Aluguel aluguel, String idEstacao, LocalDateTime fim) {
        Usuario usuario = estado.buscarUsuario(aluguel.getUsuario());
        Bicicleta bicicleta = estado.buscarBicicleta(aluguel.getBicicleta());
        estado.atualizar(usuario.adicionarPendencia(ConfiguracaoSistema.getPenalidadeAtraso())
                .comAluguelAtivo(null));
        estado.atualizar(bicicleta.colocarEmManutencao(idEstacao));
        estado.atualizar(aluguel.encerrar(Aluguel.EXPIRADO, idEstacao, fim, BigDecimal.ZERO));
    }

    
    private void registrarDevolucao(Aluguel aluguel, String idEstacao, LocalDateTime fim, long minutos) {
        Usuario usuario = estado.buscarUsuario(aluguel.getUsuario());
        Bicicleta bicicleta = estado.buscarBicicleta(aluguel.getBicicleta());
        boolean cancelado = aluguel.devolvidoNaCarencia(idEstacao, minutos);
        BigDecimal valor = cancelado ? BigDecimal.ZERO : calcularValor(usuario, bicicleta, minutos);
        String estadoFinal = cancelado ? Aluguel.CANCELADO : Aluguel.FINALIZADO;
        estado.atualizar(usuario.cobrar(valor).comAluguelAtivo(null));
        estado.atualizar(bicicleta.devolverNaEstacao(idEstacao));
        estado.atualizar(aluguel.encerrar(estadoFinal, idEstacao, fim, valor));
    }

    private BigDecimal calcularValor(Usuario usuario, Bicicleta bicicleta, long minutos) {
        BigDecimal tarifa = usuario.getPlano().calcularTarifa(minutos);
        BigDecimal adicional = bicicleta.getAdicionalPorMinuto().multiply(BigDecimal.valueOf(minutos));
        return tarifa.add(adicional).setScale(2, RoundingMode.HALF_UP);
    }

    private LocalDateTime validarData(String data) {
        if (emBranco(data)) {
            throw new AluguelException("Data nao pode ser nula.");
        }
        try {
            return FormatoDataHora.parse(data.trim());
        } catch (DateTimeException excecao) {
            throw new AluguelException("Data invalida.");
        }
    }
}
