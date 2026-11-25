package br.ufal.ic.p2.bikeshare.excecao.aluguel;
import br.ufal.ic.p2.bikeshare.dominio.aluguel.Aluguel;
public class AluguelNaoExisteException extends AluguelException {
    private static final long serialVersionUID = 1L;
    public AluguelNaoExisteException() { super("Aluguel nao existe."); }
}
