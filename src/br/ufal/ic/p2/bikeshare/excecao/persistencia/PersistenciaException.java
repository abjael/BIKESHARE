package br.ufal.ic.p2.bikeshare.excecao.persistencia;

import br.ufal.ic.p2.bikeshare.excecao.BikeShareException;

public abstract class PersistenciaException extends BikeShareException {
    private static final long serialVersionUID = 1L;

    protected PersistenciaException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }

    protected PersistenciaException(String mensagem) {
        super(mensagem);
    }
}
