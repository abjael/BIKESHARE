package br.ufal.ic.p2.bikeshare.excecao.aluguel;

import br.ufal.ic.p2.bikeshare.excecao.BikeShareException;

public abstract class AluguelException extends BikeShareException {
    private static final long serialVersionUID = 1L;

    protected AluguelException(String mensagem) {
        super(mensagem);
    }
}
