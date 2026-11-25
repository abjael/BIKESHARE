package br.ufal.ic.p2.bikeshare.excecao.pagamento;

public class NumeroCartaoInvalidoException extends PagamentoException {
    private static final long serialVersionUID = 1L;

    public NumeroCartaoInvalidoException() {
        super("Numero do cartao invalido.");
    }
}
