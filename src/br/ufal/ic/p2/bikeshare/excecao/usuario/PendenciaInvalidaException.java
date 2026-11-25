package br.ufal.ic.p2.bikeshare.excecao.usuario;

public class PendenciaInvalidaException extends UsuarioException {
    private static final long serialVersionUID = 1L;

    public PendenciaInvalidaException() {
        super("Valor de pendencia invalido.");
    }
}
