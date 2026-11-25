package br.ufal.ic.p2.bikeshare.excecao.pagamento;
public class AtributoPagamentoNaoExisteException extends PagamentoException {
    private static final long serialVersionUID = 1L;
    public AtributoPagamentoNaoExisteException() { super("Atributo nao existe."); }
}
