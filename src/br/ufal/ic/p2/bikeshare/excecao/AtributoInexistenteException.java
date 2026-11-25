package br.ufal.ic.p2.bikeshare.excecao;

/** Consulta de um atributo que nao existe (usuario, bicicleta, aluguel ou pagamento). */
public class AtributoInexistenteException extends BikeShareException {
    private static final long serialVersionUID = 1L;

    public AtributoInexistenteException() {
        super("Atributo nao existe.");
    }
}
