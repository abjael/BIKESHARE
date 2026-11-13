package br.ufal.ic.p2.bikeshare.excecao.usuario;

import br.ufal.ic.p2.bikeshare.excecao.BikeShareException;

public class UsuarioException extends BikeShareException {
    private static final long serialVersionUID = 1L;

    public UsuarioException(String mensagem) {
        super(mensagem);
    }
}
