package br.ufal.ic.p2.bikeshare.persistencia;

import br.ufal.ic.p2.bikeshare.dominio.usuario.Usuario;
import br.ufal.ic.p2.bikeshare.dominio.bicicleta.Bicicleta;
import br.ufal.ic.p2.bikeshare.dominio.estacao.Estacao;
import br.ufal.ic.p2.bikeshare.dominio.aluguel.Aluguel;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;


public class EstadoPersistido {
    private final LinkedHashMap<String, Usuario> usuarios;
    private final int proximoIdUsuario;
    private final LinkedHashMap<String, Estacao> estacoes;
    private final int proximoIdEstacao;
    private final LinkedHashMap<String, Bicicleta> bicicletas;
    private final LinkedHashMap<String, Aluguel> alugueis;
    private final int proximoIdAluguel;

    public EstadoPersistido(Map<String, Usuario> usuarios, int proximoIdUsuario) {
        this(usuarios, proximoIdUsuario, Collections.emptyMap(), 1);
    }

    public EstadoPersistido(Map<String, Usuario> usuarios, int proximoIdUsuario,
                            Map<String, Estacao> estacoes, int proximoIdEstacao) {
        this(usuarios, proximoIdUsuario, estacoes, proximoIdEstacao, Collections.emptyMap());
    }

    public EstadoPersistido(Map<String, Usuario> usuarios, int proximoIdUsuario,
                            Map<String, Estacao> estacoes, int proximoIdEstacao,
                            Map<String, Bicicleta> bicicletas) {
        this(usuarios, proximoIdUsuario, estacoes, proximoIdEstacao, bicicletas,
                Collections.emptyMap(), 1);
    }

    public EstadoPersistido(Map<String, Usuario> usuarios, int proximoIdUsuario,
                            Map<String, Estacao> estacoes, int proximoIdEstacao,
                            Map<String, Bicicleta> bicicletas, Map<String, Aluguel> alugueis,
                            int proximoIdAluguel) {
        this.usuarios = new LinkedHashMap<>(usuarios);
        this.proximoIdUsuario = proximoIdUsuario;
        this.estacoes = new LinkedHashMap<>(estacoes);
        this.proximoIdEstacao = proximoIdEstacao;
        this.bicicletas = new LinkedHashMap<>(bicicletas);
        this.alugueis = new LinkedHashMap<>(alugueis);
        this.proximoIdAluguel = proximoIdAluguel;
    }

    
    public static EstadoPersistido vazio() {
        return new EstadoPersistido(Collections.emptyMap(), 1, Collections.emptyMap(), 1,
                Collections.emptyMap(), Collections.emptyMap(), 1);
    }

    public Map<String, Usuario> getUsuarios() {
        return new LinkedHashMap<>(usuarios);
    }

    public int getProximoIdUsuario() {
        return proximoIdUsuario;
    }

    public Map<String, Estacao> getEstacoes() {
        return new LinkedHashMap<>(estacoes);
    }

    public int getProximoIdEstacao() {
        return proximoIdEstacao;
    }

    public Map<String, Bicicleta> getBicicletas() {
        return new LinkedHashMap<>(bicicletas);
    }

    public Map<String, Aluguel> getAlugueis() {
        return new LinkedHashMap<>(alugueis);
    }

    public int getProximoIdAluguel() {
        return proximoIdAluguel;
    }
}
