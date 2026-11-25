package br.ufal.ic.p2.bikeshare.excecao.bicicleta;

public class BicicletaNaoEstaEmUsoException extends BicicletaException {
    private static final long serialVersionUID = 1L;

    public BicicletaNaoEstaEmUsoException() {
        super("Bicicleta nao esta em uso.");
    }
}
