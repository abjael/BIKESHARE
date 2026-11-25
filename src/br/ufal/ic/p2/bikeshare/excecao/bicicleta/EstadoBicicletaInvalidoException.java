package br.ufal.ic.p2.bikeshare.excecao.bicicleta;

public class EstadoBicicletaInvalidoException extends BicicletaException {
    private static final long serialVersionUID = 1L;

    public EstadoBicicletaInvalidoException() {
        super("Estado de bicicleta invalido.");
    }
}
