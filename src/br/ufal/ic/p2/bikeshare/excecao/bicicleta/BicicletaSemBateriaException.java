package br.ufal.ic.p2.bikeshare.excecao.bicicleta;

public class BicicletaSemBateriaException extends BicicletaException {
    private static final long serialVersionUID = 1L;
    public BicicletaSemBateriaException() { super("Bicicleta nao possui bateria."); }
}
