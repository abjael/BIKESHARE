package br.ufal.ic.p2.bikeshare.excecao.usuario;
public class AtributoNaoPodeSerAlteradoException extends UsuarioException {
    private static final long serialVersionUID = 1L;
    public AtributoNaoPodeSerAlteradoException() { super("Atributo nao pode ser alterado."); }
}
