package br.ufal.ic.p2.bikeshare.excecao.bicicleta;
import br.ufal.ic.p2.bikeshare.dominio.bicicleta.Bicicleta;
public class BicicletaSemBateriaException extends BicicletaException {
    private static final long serialVersionUID = 1L;
    public BicicletaSemBateriaException() { super("Bicicleta nao possui bateria."); }
}
