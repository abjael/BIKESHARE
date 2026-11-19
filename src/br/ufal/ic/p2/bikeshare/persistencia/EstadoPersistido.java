package br.ufal.ic.p2.bikeshare.persistencia;

import br.ufal.ic.p2.bikeshare.dominio.Estacao;
import br.ufal.ic.p2.bikeshare.dominio.Usuario;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

public class EstadoPersistido implements Serializable {
    private static final long serialVersionUID = 1L;

    private final LinkedHashMap<String, Usuario> usuarios;
    private final int proximoIdUsuario;
    private final LinkedHashMap<String, Estacao> estacoes;
    private final int proximoIdEstacao;

    public EstadoPersistido(Map<String, Usuario> usuarios, int proximoIdUsuario) {
        this(usuarios, proximoIdUsuario, java.util.Collections.emptyMap(), 1);
    }

    public EstadoPersistido(Map<String, Usuario> usuarios, int proximoIdUsuario,
                            Map<String, Estacao> estacoes, int proximoIdEstacao) {
        this.usuarios = new LinkedHashMap<>(usuarios);
        this.proximoIdUsuario = proximoIdUsuario;
        this.estacoes = new LinkedHashMap<>(estacoes);
        this.proximoIdEstacao = proximoIdEstacao;
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
}
