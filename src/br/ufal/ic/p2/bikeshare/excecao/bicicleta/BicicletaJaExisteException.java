package br.ufal.ic.p2.bikeshare.excecao.bicicleta;

public class BicicletaJaExisteException extends BicicletaException {
    private static final long serialVersionUID = 1L;
    public BicicletaJaExisteException() { super("Bicicleta ja existe."); }
}
