package br.ufal.ic.p2.bikeshare.servico;

import br.ufal.ic.p2.bikeshare.dominio.Aluguel;
import br.ufal.ic.p2.bikeshare.dominio.Bicicleta;
import br.ufal.ic.p2.bikeshare.dominio.Estacao;
import br.ufal.ic.p2.bikeshare.dominio.Usuario;
import br.ufal.ic.p2.bikeshare.excecao.BikeShareException;
import br.ufal.ic.p2.bikeshare.excecao.aluguel.AluguelException;
import br.ufal.ic.p2.bikeshare.excecao.bicicleta.BicicletaException;
import br.ufal.ic.p2.bikeshare.excecao.estacao.EstacaoException;
import br.ufal.ic.p2.bikeshare.excecao.usuario.UsuarioException;
import br.ufal.ic.p2.bikeshare.persistencia.EstadoPersistido;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;


final class EstadoSistema {
    private final Map<String, Usuario> usuarios = new LinkedHashMap<>();
    private final Map<String, Estacao> estacoes = new LinkedHashMap<>();
    private final Map<String, Bicicleta> bicicletas = new LinkedHashMap<>();
    private final Map<String, Aluguel> alugueis = new LinkedHashMap<>();
    private int proximoIdUsuario;
    private int proximoIdEstacao;
    private int proximoIdAluguel;

    EstadoSistema(EstadoPersistido inicial) {
        restaurar(inicial);
    }

    EstadoPersistido capturar() {
        return new EstadoPersistido(usuarios, proximoIdUsuario, estacoes, proximoIdEstacao,
                bicicletas, alugueis, proximoIdAluguel);
    }

    void restaurar(EstadoPersistido estado) {
        substituir(usuarios, estado.getUsuarios());
        substituir(estacoes, estado.getEstacoes());
        substituir(bicicletas, estado.getBicicletas());
        substituir(alugueis, estado.getAlugueis());
        proximoIdUsuario = estado.getProximoIdUsuario();
        proximoIdEstacao = estado.getProximoIdEstacao();
        proximoIdAluguel = estado.getProximoIdAluguel();
    }

    String gerarIdUsuario() {
        return "U" + proximoIdUsuario++;
    }

    String gerarIdEstacao() {
        return "E" + proximoIdEstacao++;
    }

    String gerarIdAluguel() {
        return "A" + proximoIdAluguel++;
    }

    Usuario buscarUsuario(String id) {
        return buscar(usuarios, id, UsuarioException::new,
                "Identificacao do usuario nao pode ser nula.", "Usuario nao existe.");
    }

    Estacao buscarEstacao(String id) {
        return buscar(estacoes, id, EstacaoException::new,
                "Identificacao da estacao nao pode ser nula.", "Estacao nao existe.");
    }

    Bicicleta buscarBicicleta(String id) {
        return buscar(bicicletas, id, BicicletaException::new,
                "Identificacao da bicicleta nao pode ser nula.", "Bicicleta nao existe.");
    }

    Aluguel buscarAluguel(String id) {
        return buscar(alugueis, id, AluguelException::new,
                "Identificacao do aluguel nao pode ser nula.", "Aluguel nao existe.");
    }

    
    Usuario buscarUsuarioPorEmail(String email) {
        String chave = ValidadorUsuario.normalizarEmail(email);
        for (Usuario usuario : usuarios.values()) {
            if (ValidadorUsuario.normalizarEmail(usuario.getEmail()).equals(chave)) {
                return usuario;
            }
        }
        return null;
    }

    boolean existeBicicleta(String id) {
        return bicicletas.containsKey(id);
    }

    Collection<Bicicleta> getBicicletas() {
        return bicicletas.values();
    }

    void atualizar(Usuario usuario) {
        usuarios.put(usuario.getId(), usuario);
    }

    void atualizar(Estacao estacao) {
        estacoes.put(estacao.getId(), estacao);
    }

    void atualizar(Bicicleta bicicleta) {
        bicicletas.put(bicicleta.getId(), bicicleta);
    }

    void atualizar(Aluguel aluguel) {
        alugueis.put(aluguel.getId(), aluguel);
    }

    void removerUsuario(String id) {
        usuarios.remove(id);
    }

    void removerBicicleta(String id) {
        bicicletas.remove(id);
    }

    private static <T> T buscar(Map<String, T> mapa, String id, Function<String, BikeShareException> erro,
                                String mensagemIdNulo, String mensagemInexistente) {
        if (ValidadorEntrada.emBranco(id)) {
            throw erro.apply(mensagemIdNulo);
        }
        T entidade = mapa.get(id);
        if (entidade == null) {
            throw erro.apply(mensagemInexistente);
        }
        return entidade;
    }

    private static <T> void substituir(Map<String, T> destino, Map<String, T> origem) {
        destino.clear();
        destino.putAll(origem);
    }
}
