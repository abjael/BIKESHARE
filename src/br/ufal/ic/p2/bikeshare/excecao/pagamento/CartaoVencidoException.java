package br.ufal.ic.p2.bikeshare.excecao.pagamento;
import br.ufal.ic.p2.bikeshare.dominio.pagamento.Cartao;
public class CartaoVencidoException extends PagamentoException {
    private static final long serialVersionUID = 1L;
    public CartaoVencidoException() { super("Cartao vencido."); }
}
