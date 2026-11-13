package br.ufal.ic.p2.bikeshare.persistencia;

import br.ufal.ic.p2.bikeshare.dominio.Usuario;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

public class EstadoPersistido implements Serializable {
    private static final long serialVersionUID = 1L;

    private final LinkedHashMap<String, Usuario> usuarios;
    private final int proximoIdUsuario;

    public EstadoPersistido(Map<String, Usuario> usuarios, int proximoIdUsuario) {
        this.usuarios = new LinkedHashMap<>(usuarios);
        this.proximoIdUsuario = proximoIdUsuario;
    }

    public Map<String, Usuario> getUsuarios() {
        return new LinkedHashMap<>(usuarios);
    }

    public int getProximoIdUsuario() {
        return proximoIdUsuario;
    }
}
