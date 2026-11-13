package br.ufal.ic.p2.bikeshare.excecao;

public class BikeShareException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public BikeShareException(String mensagem) {
        super(mensagem);
    }

    public BikeShareException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
