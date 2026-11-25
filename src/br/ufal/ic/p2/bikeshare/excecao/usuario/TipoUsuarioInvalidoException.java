package br.ufal.ic.p2.bikeshare.excecao.usuario;
public class TipoUsuarioInvalidoException extends UsuarioException {
    private static final long serialVersionUID = 1L;
    public TipoUsuarioInvalidoException() { super("Tipo invalido."); }
}
