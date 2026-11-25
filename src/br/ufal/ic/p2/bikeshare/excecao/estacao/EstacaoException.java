package br.ufal.ic.p2.bikeshare.excecao.estacao;

import br.ufal.ic.p2.bikeshare.excecao.BikeShareException;

public abstract class EstacaoException extends BikeShareException {
    private static final long serialVersionUID = 1L;

    protected EstacaoException(String mensagem) {
        super(mensagem);
    }
}
