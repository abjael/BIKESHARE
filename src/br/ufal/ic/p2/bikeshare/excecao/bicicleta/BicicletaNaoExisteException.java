package br.ufal.ic.p2.bikeshare.excecao.bicicleta;

public class BicicletaNaoExisteException extends BicicletaException {
    private static final long serialVersionUID = 1L;
    public BicicletaNaoExisteException() { super("Bicicleta nao existe."); }
}
