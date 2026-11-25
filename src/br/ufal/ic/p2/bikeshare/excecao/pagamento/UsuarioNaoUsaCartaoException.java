package br.ufal.ic.p2.bikeshare.excecao.pagamento;

public class UsuarioNaoUsaCartaoException extends PagamentoException {
    private static final long serialVersionUID = 1L;
    public UsuarioNaoUsaCartaoException() { super("Usuario nao usa cartao."); }
}
