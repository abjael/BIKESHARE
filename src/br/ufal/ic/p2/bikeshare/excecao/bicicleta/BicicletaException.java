package br.ufal.ic.p2.bikeshare.excecao.bicicleta;

import br.ufal.ic.p2.bikeshare.excecao.BikeShareException;

public abstract class BicicletaException extends BikeShareException {
    private static final long serialVersionUID = 1L;

    public BicicletaException(String mensagem) {
        super(mensagem);
    }
}
