package br.ufal.ic.p2.bikeshare.servico.aluguel;

import br.ufal.ic.p2.bikeshare.servico.estado.EstadoSistema;
import br.ufal.ic.p2.bikeshare.servico.validacao.ValidadorEntrada;
import br.ufal.ic.p2.bikeshare.dominio.usuario.Usuario;
import br.ufal.ic.p2.bikeshare.dominio.bicicleta.Bicicleta;
import br.ufal.ic.p2.bikeshare.dominio.estacao.Estacao;
import br.ufal.ic.p2.bikeshare.dominio.aluguel.Aluguel;
import br.ufal.ic.p2.bikeshare.dominio.util.ConfiguracaoSistema;
import br.ufal.ic.p2.bikeshare.dominio.util.FormatoDataHora;
import br.ufal.ic.p2.bikeshare.excecao.aluguel.AluguelException;
import br.ufal.ic.p2.bikeshare.excecao.bicicleta.BicicletaException;
import br.ufal.ic.p2.bikeshare.excecao.estacao.EstacaoException;
import br.ufal.ic.p2.bikeshare.excecao.usuario.UsuarioException;
import br.ufal.ic.p2.bikeshare.excecao.usuario.UsuarioInadimplenteException;
import br.ufal.ic.p2.bikeshare.excecao.usuario.UsuarioJaPossuiAluguelException;
import br.ufal.ic.p2.bikeshare.excecao.bicicleta.BicicletaNaoEstaNaEstacaoException;
import br.ufal.ic.p2.bikeshare.excecao.aluguel.AluguelNaoEstaEmAndamentoException;
import br.ufal.ic.p2.bikeshare.excecao.aluguel.DataDevolucaoAnteriorException;
import br.ufal.ic.p2.bikeshare.excecao.aluguel.DataNaoPodeSerNulaException;
import br.ufal.ic.p2.bikeshare.excecao.aluguel.DataInvalidaException;
import br.ufal.ic.p2.bikeshare.excecao.estacao.EstacaoCheiaException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DateTimeException;
import java.time.LocalDateTime;

import static br.ufal.ic.p2.bikeshare.servico.validacao.ValidadorEntrada.emBranco;


public final class ServicoAluguel {
    private final EstadoSistema estado;

    public ServicoAluguel(EstadoSistema estado) {
        this.estado = estado;
    }

    public String retirarBicicleta(String idUsuario, String idEstacao, String idBicicleta, String data) {
        Usuario usuario = estado.buscarUsuario(idUsuario);
        if (usuario.estaInadimplente()) {
            throw new UsuarioInadimplenteException();
        }
        Estacao estacao = estado.buscarEstacao(idEstacao);
        Bicicleta bicicleta = estado.buscarBicicleta(idBicicleta);
        LocalDateTime inicio = validarData(data);
        validarRetirada(usuario, bicicleta, idEstacao, inicio);

        Aluguel aluguel = new Aluguel(estado.gerarIdAluguel(), idUsuario, idBicicleta, idEstacao, inicio);
        estado.atualizar(aluguel);
        estado.atualizar(bicicleta.retirarDaEstacao());
        estado.atualizar(usuario.comAluguelAtivo(aluguel.getId()));
        return aluguel.getId();
    }

    public void devolverBicicleta(String idAluguel, String idEstacao, String data) {
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
    }

    public String getAtributo(String id, String atributo) {
        return estado.buscarAluguel(id).getAtributo(atributo);
    }

    private void validarRetirada(Usuario usuario, Bicicleta bicicleta, String idEstacao, LocalDateTime inicio) {
        if (usuario.possuiAluguelAtivo()) {
            throw new UsuarioJaPossuiAluguelException();
        }
        if (!bicicleta.estaNaEstacao(idEstacao)) {
            throw new BicicletaNaoEstaNaEstacaoException();
        }
        bicicleta.validarRetirada();
        usuario.validarPagamentoPendente(inicio);
    }

    private void validarDevolucao(Aluguel aluguel, Estacao destino, LocalDateTime fim) {
        if (!aluguel.estaEmAndamento()) {
            throw new AluguelNaoEstaEmAndamentoException();
        }
        if (fim.isBefore(aluguel.getInicio())) {
            throw new DataDevolucaoAnteriorException();
        }
        if (!estado.estacaoTemVaga(destino)) {
            throw new EstacaoCheiaException();
        }
    }

    
    private void registrarExpiracao(Aluguel aluguel, String idEstacao, LocalDateTime fim) {
        Usuario usuario = estado.buscarUsuario(aluguel.getUsuario());
        Bicicleta bicicleta = estado.buscarBicicleta(aluguel.getBicicleta());
        estado.atualizar(usuario.adicionarPendencia(ConfiguracaoSistema.getPenalidadeAtraso())
                .comAluguelAtivo(null));
        estado.atualizar(bicicleta.colocarEmManutencao(idEstacao));
        estado.atualizar(aluguel.expirar(idEstacao, fim));
    }

    
    private void registrarDevolucao(Aluguel aluguel, String idEstacao, LocalDateTime fim, long minutos) {
        Usuario usuario = estado.buscarUsuario(aluguel.getUsuario());
        Bicicleta bicicleta = estado.buscarBicicleta(aluguel.getBicicleta());
        boolean cancelado = aluguel.devolvidoNaCarencia(idEstacao, minutos);
        BigDecimal valor = cancelado ? BigDecimal.ZERO : calcularValor(usuario, bicicleta, minutos);
        estado.atualizar(usuario.cobrar(valor).comAluguelAtivo(null));
        estado.atualizar(bicicleta.devolverNaEstacao(idEstacao));
        estado.atualizar(cancelado ? aluguel.cancelar(idEstacao, fim)
                : aluguel.finalizar(idEstacao, fim, valor));
    }

    private BigDecimal calcularValor(Usuario usuario, Bicicleta bicicleta, long minutos) {
        BigDecimal tarifa = usuario.getPlano().calcularTarifa(minutos);
        BigDecimal adicional = bicicleta.getAdicionalPorMinuto().multiply(BigDecimal.valueOf(minutos));
        return tarifa.add(adicional).setScale(2, RoundingMode.HALF_UP);
    }

    private LocalDateTime validarData(String data) {
        if (emBranco(data)) {
            throw new DataNaoPodeSerNulaException();
        }
        try {
            return FormatoDataHora.parse(data.trim());
        } catch (DateTimeException excecao) {
            throw new DataInvalidaException();
        }
    }
}
