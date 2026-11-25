package br.ufal.ic.p2.bikeshare.excecao;

/** Tipo informado nao existe (tipo de usuario ou de bicicleta). */
public class TipoInvalidoException extends BikeShareException {
    private static final long serialVersionUID = 1L;

    public TipoInvalidoException() {
        super("Tipo invalido.");
    }
}
