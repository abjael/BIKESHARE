package br.ufal.ic.p2.bikeshare.excecao.pagamento;
public class ValidadeCartaoInvalidaException extends PagamentoException {
    private static final long serialVersionUID = 1L;
    public ValidadeCartaoInvalidaException() { super("Validade do cartao invalida."); }
}
