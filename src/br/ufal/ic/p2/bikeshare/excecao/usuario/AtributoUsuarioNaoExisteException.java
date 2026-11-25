package br.ufal.ic.p2.bikeshare.excecao.usuario;
public class AtributoUsuarioNaoExisteException extends UsuarioException {
    private static final long serialVersionUID = 1L;
    public AtributoUsuarioNaoExisteException() { super("Atributo nao existe."); }
}
