package br.ufal.ic.p2.bikeshare.excecao.plano;
import br.ufal.ic.p2.bikeshare.dominio.plano.Plano;
public class PlanoInvalidoException extends PlanoException {
    private static final long serialVersionUID = 1L;
    public PlanoInvalidoException() { super("Plano invalido."); }
}
