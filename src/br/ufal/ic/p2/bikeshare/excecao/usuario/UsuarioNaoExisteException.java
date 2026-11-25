package br.ufal.ic.p2.bikeshare.excecao.usuario;

public class UsuarioNaoExisteException extends UsuarioException {
    private static final long serialVersionUID = 1L;
    public UsuarioNaoExisteException() { super("Usuario nao existe."); }
}
