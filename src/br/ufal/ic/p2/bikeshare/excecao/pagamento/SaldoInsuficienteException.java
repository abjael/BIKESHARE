package br.ufal.ic.p2.bikeshare.excecao.pagamento;

public class SaldoInsuficienteException extends PagamentoException {
    private static final long serialVersionUID = 1L;
    public SaldoInsuficienteException() { super("Saldo insuficiente."); }
}
