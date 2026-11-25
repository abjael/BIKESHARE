package br.ufal.ic.p2.bikeshare.excecao.usuario;

public class UsuarioInadimplenteException extends UsuarioException {
    private static final long serialVersionUID = 1L;
    public UsuarioInadimplenteException() { super("Usuario inadimplente."); }
}
