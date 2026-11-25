package br.ufal.ic.p2.bikeshare.excecao.pagamento;

import br.ufal.ic.p2.bikeshare.excecao.BikeShareException;

public abstract class PagamentoException extends BikeShareException {
    private static final long serialVersionUID = 1L;
    protected PagamentoException(String mensagem) { super(mensagem); }
}
