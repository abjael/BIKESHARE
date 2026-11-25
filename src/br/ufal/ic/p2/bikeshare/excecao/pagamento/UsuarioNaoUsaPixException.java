package br.ufal.ic.p2.bikeshare.excecao.pagamento;

public class UsuarioNaoUsaPixException extends PagamentoException {
    private static final long serialVersionUID = 1L;
    public UsuarioNaoUsaPixException() { super("Usuario nao usa pix."); }
}
