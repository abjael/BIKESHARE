package br.ufal.ic.p2.bikeshare.excecao.pagamento;

public class SaldoNegativoException extends PagamentoException {
    private static final long serialVersionUID = 1L;

    public SaldoNegativoException() {
        super("Saldo nao pode ser negativo.");
    }
}
