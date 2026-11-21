package br.ufal.ic.p2.bikeshare.servico;

import br.ufal.ic.p2.bikeshare.dominio.Aluguel;
import br.ufal.ic.p2.bikeshare.dominio.Bicicleta;
import br.ufal.ic.p2.bikeshare.dominio.Estacao;
import br.ufal.ic.p2.bikeshare.dominio.FormatoDataHora;
import br.ufal.ic.p2.bikeshare.dominio.Usuario;
import br.ufal.ic.p2.bikeshare.excecao.aluguel.AluguelException;
import br.ufal.ic.p2.bikeshare.excecao.bicicleta.BicicletaException;
import br.ufal.ic.p2.bikeshare.excecao.estacao.EstacaoException;
import br.ufal.ic.p2.bikeshare.excecao.usuario.UsuarioException;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.Map;

import static br.ufal.ic.p2.bikeshare.servico.ValidadorUsuario.emBranco;

final class ServicoAluguel {
    private final Map<String, Usuario> usuarios;
    private final Map<String, Estacao> estacoes;
    private final Map<String, Bicicleta> bicicletas;
    private final Map<String, Aluguel> alugueis;

    ServicoAluguel(Map<String, Usuario> usuarios, Map<String, Estacao> estacoes,
                   Map<String, Bicicleta> bicicletas, Map<String, Aluguel> alugueis) {
        this.usuarios = usuarios;
        this.estacoes = estacoes;
        this.bicicletas = bicicletas;
        this.alugueis = alugueis;
    }

    String retirarBicicleta(String idUsuario, String idEstacao, String idBicicleta, String data, int idAluguel) {
        Usuario usuario = buscarUsuario(idUsuario);
        Estacao estacao = buscarEstacao(idEstacao);
        Bicicleta bicicleta = buscarBicicleta(idBicicleta);
        LocalDateTime inicio = validarData(data);
        validarUsuarioSemAluguelAtivo(usuario);
        validarBicicletaNaEstacao(bicicleta, idEstacao);
        bicicleta.validarRetirada();
        usuario.validarPagamentoPendente();

        String id = "A" + idAluguel;
        Aluguel aluguel = new Aluguel(id, idUsuario, idBicicleta, idEstacao,
                FormatoDataHora.formatar(inicio));
        estacoes.put(idEstacao, estacao.removerBicicleta());
        bicicletas.put(idBicicleta, bicicleta.retirarDaEstacao());
        usuarios.put(idUsuario, usuario.comAluguelAtivo(id));
        alugueis.put(id, aluguel);
        return id;
    }

    String getAtributo(String id, String atributo) {
        return buscarAluguel(id).getAtributo(atributo);
    }

    private Usuario buscarUsuario(String id) {
        if (emBranco(id)) {
            throw new UsuarioException("Identificacao do usuario nao pode ser nula.");
        }
        Usuario usuario = usuarios.get(id);
        if (usuario == null) {
            throw new UsuarioException("Usuario nao existe.");
        }
        return usuario;
    }

    private Estacao buscarEstacao(String id) {
        if (emBranco(id)) {
            throw new EstacaoException("Identificacao da estacao nao pode ser nula.");
        }
        Estacao estacao = estacoes.get(id);
        if (estacao == null) {
            throw new EstacaoException("Estacao nao existe.");
        }
        return estacao;
    }

    private Bicicleta buscarBicicleta(String id) {
        if (emBranco(id)) {
            throw new BicicletaException("Identificacao da bicicleta nao pode ser nula.");
        }
        Bicicleta bicicleta = bicicletas.get(id);
        if (bicicleta == null) {
            throw new BicicletaException("Bicicleta nao existe.");
        }
        return bicicleta;
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

    private void validarUsuarioSemAluguelAtivo(Usuario usuario) {
        if (usuario.getAluguelAtivo() != null) {
            throw new UsuarioException("Usuario ja possui um aluguel em andamento.");
        }
    }

    private void validarBicicletaNaEstacao(Bicicleta bicicleta, String idEstacao) {
        if (!idEstacao.equals(bicicleta.getEstacao())) {
            throw new BicicletaException("Bicicleta nao esta nesta estacao.");
        }
    }

    private Aluguel buscarAluguel(String id) {
        if (emBranco(id)) {
            throw new AluguelException("Identificacao do aluguel nao pode ser nula.");
        }
        Aluguel aluguel = alugueis.get(id);
        if (aluguel == null) {
            throw new AluguelException("Aluguel nao existe.");
        }
        return aluguel;
    }
}
