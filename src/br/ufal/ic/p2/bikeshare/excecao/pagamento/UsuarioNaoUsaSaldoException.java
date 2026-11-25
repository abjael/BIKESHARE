package br.ufal.ic.p2.bikeshare.excecao.pagamento;

public class UsuarioNaoUsaSaldoException extends PagamentoException {
    private static final long serialVersionUID = 1L;
    public UsuarioNaoUsaSaldoException() { super("Usuario nao usa saldo pre-carregado."); }
}
