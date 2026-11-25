package br.ufal.ic.p2.bikeshare.excecao.pagamento;
public class ValorMetodoPagamentoInvalidoException extends PagamentoException {
    private static final long serialVersionUID = 1L;
    public ValorMetodoPagamentoInvalidoException() { super("Valor de metodo de pagamento invalido."); }
}
