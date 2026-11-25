package br.ufal.ic.p2.bikeshare.excecao.plano;
import br.ufal.ic.p2.bikeshare.excecao.BikeShareException;
public abstract class PlanoException extends BikeShareException {
    private static final long serialVersionUID = 1L;
    protected PlanoException(String mensagem) { super(mensagem); }
}
