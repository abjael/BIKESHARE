package br.ufal.ic.p2.bikeshare.excecao.usuario;
public class EmailJaCadastradoException extends UsuarioException {
    private static final long serialVersionUID = 1L;
    public EmailJaCadastradoException() { super("Email ja cadastrado."); }
}
