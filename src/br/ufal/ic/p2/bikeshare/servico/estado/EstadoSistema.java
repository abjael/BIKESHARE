package br.ufal.ic.p2.bikeshare.servico.estado;

import br.ufal.ic.p2.bikeshare.servico.usuario.ValidadorUsuario;
import br.ufal.ic.p2.bikeshare.servico.validacao.ValidadorEntrada;
import br.ufal.ic.p2.bikeshare.dominio.usuario.Usuario;
import br.ufal.ic.p2.bikeshare.dominio.bicicleta.Bicicleta;
import br.ufal.ic.p2.bikeshare.dominio.estacao.Estacao;
import br.ufal.ic.p2.bikeshare.dominio.aluguel.Aluguel;
import br.ufal.ic.p2.bikeshare.excecao.aluguel.AluguelException;
import br.ufal.ic.p2.bikeshare.excecao.aluguel.IdentificacaoAluguelNulaException;
import br.ufal.ic.p2.bikeshare.excecao.aluguel.AluguelNaoExisteException;
import br.ufal.ic.p2.bikeshare.excecao.bicicleta.BicicletaException;
import br.ufal.ic.p2.bikeshare.excecao.bicicleta.IdentificacaoBicicletaNulaException;
import br.ufal.ic.p2.bikeshare.excecao.bicicleta.BicicletaNaoExisteException;
import br.ufal.ic.p2.bikeshare.excecao.estacao.EstacaoException;
import br.ufal.ic.p2.bikeshare.excecao.estacao.IdentificacaoEstacaoNulaException;
import br.ufal.ic.p2.bikeshare.excecao.estacao.EstacaoNaoExisteException;
import br.ufal.ic.p2.bikeshare.excecao.usuario.UsuarioException;
import br.ufal.ic.p2.bikeshare.excecao.usuario.IdentificacaoUsuarioNulaException;
import br.ufal.ic.p2.bikeshare.excecao.usuario.UsuarioNaoExisteException;
import br.ufal.ic.p2.bikeshare.persistencia.EstadoPersistido;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;


public final class EstadoSistema {
    private final Map<String, Usuario> usuarios = new LinkedHashMap<>();
    private final Map<String, Estacao> estacoes = new LinkedHashMap<>();
    private final Map<String, Bicicleta> bicicletas = new LinkedHashMap<>();
    private final Map<String, Aluguel> alugueis = new LinkedHashMap<>();
    private int proximoIdUsuario;
    private int proximoIdEstacao;
    private int proximoIdAluguel;

    public EstadoSistema(EstadoPersistido inicial) {
        restaurar(inicial);
    }

    public EstadoPersistido capturar() {
        return new EstadoPersistido(usuarios, proximoIdUsuario, estacoes, proximoIdEstacao,
                bicicletas, alugueis, proximoIdAluguel);
    }

    public void restaurar(EstadoPersistido estado) {
        substituir(usuarios, estado.getUsuarios());
        substituir(estacoes, estado.getEstacoes());
        substituir(bicicletas, estado.getBicicletas());
        substituir(alugueis, estado.getAlugueis());
        proximoIdUsuario = estado.getProximoIdUsuario();
        proximoIdEstacao = estado.getProximoIdEstacao();
        proximoIdAluguel = estado.getProximoIdAluguel();
    }

    public String gerarIdUsuario() {
        return "U" + proximoIdUsuario++;
    }

    public String gerarIdEstacao() {
        return "E" + proximoIdEstacao++;
    }

    public String gerarIdAluguel() {
        return "A" + proximoIdAluguel++;
    }

    public Usuario buscarUsuario(String id) {
        id = id == null ? null : id.trim();
        if (ValidadorEntrada.emBranco(id)) {
            throw new IdentificacaoUsuarioNulaException();
        }
        Usuario entidade = usuarios.get(id);
        if (entidade == null) {
            throw new UsuarioNaoExisteException();
        }
        return entidade;
    }

    public Estacao buscarEstacao(String id) {
        id = id == null ? null : id.trim();
        if (ValidadorEntrada.emBranco(id)) {
            throw new IdentificacaoEstacaoNulaException();
        }
        Estacao entidade = estacoes.get(id);
        if (entidade == null) {
            throw new EstacaoNaoExisteException();
        }
        return entidade;
    }

    public Bicicleta buscarBicicleta(String id) {
        id = id == null ? null : id.trim();
        if (ValidadorEntrada.emBranco(id)) {
            throw new IdentificacaoBicicletaNulaException();
        }
        Bicicleta entidade = bicicletas.get(id);
        if (entidade == null) {
            throw new BicicletaNaoExisteException();
        }
        return entidade;
    }

    public Aluguel buscarAluguel(String id) {
        id = id == null ? null : id.trim();
        if (ValidadorEntrada.emBranco(id)) {
            throw new IdentificacaoAluguelNulaException();
        }
        Aluguel entidade = alugueis.get(id);
        if (entidade == null) {
            throw new AluguelNaoExisteException();
        }
        return entidade;
    }

    
    public Optional<Usuario> buscarUsuarioPorEmail(String email) {
        String chave = ValidadorUsuario.normalizarEmail(email);
        for (Usuario usuario : usuarios.values()) {
            if (ValidadorUsuario.normalizarEmail(usuario.getEmail()).equals(chave)) {
                return Optional.of(usuario);
            }
        }
        return Optional.empty();
    }

    public boolean existeBicicleta(String id) {
        return bicicletas.containsKey(id);
    }

    public Collection<Bicicleta> getBicicletas() {
        return Collections.unmodifiableCollection(bicicletas.values());
    }

    public int contarBicicletasNaEstacao(String idEstacao) {
        int quantidade = 0;
        for (Bicicleta bicicleta : bicicletas.values()) {
            if (bicicleta.estaNaEstacao(idEstacao)) {
                quantidade++;
            }
        }
        return quantidade;
    }

    public int getVagasDisponiveis(Estacao estacao) {
        return estacao.getCapacidade() - contarBicicletasNaEstacao(estacao.getId());
    }

    public boolean estacaoTemVaga(Estacao estacao) {
        return getVagasDisponiveis(estacao) > 0;
    }

    public void atualizar(Usuario usuario) {
        usuarios.put(usuario.getId(), usuario);
    }

    public void atualizar(Estacao estacao) {
        estacoes.put(estacao.getId(), estacao);
    }

    public void atualizar(Bicicleta bicicleta) {
        bicicletas.put(bicicleta.getId(), bicicleta);
    }

    public void atualizar(Aluguel aluguel) {
        alugueis.put(aluguel.getId(), aluguel);
    }

    public void removerUsuario(String id) {
        usuarios.remove(id);
    }

    public void removerBicicleta(String id) {
        bicicletas.remove(id);
    }

    private static <T> void substituir(Map<String, T> destino, Map<String, T> origem) {
        destino.clear();
        destino.putAll(origem);
    }
}
