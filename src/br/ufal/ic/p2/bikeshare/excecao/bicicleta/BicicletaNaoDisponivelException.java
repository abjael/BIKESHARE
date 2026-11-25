package br.ufal.ic.p2.bikeshare.excecao.bicicleta;
import br.ufal.ic.p2.bikeshare.dominio.bicicleta.Bicicleta;
public class BicicletaNaoDisponivelException extends BicicletaException {
    private static final long serialVersionUID = 1L;
    public BicicletaNaoDisponivelException() { super("Bicicleta nao esta disponivel."); }
}
