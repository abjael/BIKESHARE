package br.ufal.ic.p2.bikeshare.excecao.usuario;
public class EmailInvalidoException extends UsuarioException {
    private static final long serialVersionUID = 1L;
    public EmailInvalidoException() { super("Email invalido."); }
}
