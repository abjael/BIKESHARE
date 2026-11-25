package br.ufal.ic.p2.bikeshare.excecao;

public abstract class BikeShareException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    protected BikeShareException(String mensagem) {
        super(mensagem);
    }

    protected BikeShareException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
