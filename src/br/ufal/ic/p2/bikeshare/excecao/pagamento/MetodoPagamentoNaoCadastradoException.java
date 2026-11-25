package br.ufal.ic.p2.bikeshare.excecao.pagamento;
public class MetodoPagamentoNaoCadastradoException extends PagamentoException {
    private static final long serialVersionUID = 1L;
    public MetodoPagamentoNaoCadastradoException() { super("Metodo de pagamento nao cadastrado."); }
}
