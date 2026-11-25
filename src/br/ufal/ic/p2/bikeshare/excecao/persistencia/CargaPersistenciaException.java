package br.ufal.ic.p2.bikeshare.excecao.persistencia;

public class CargaPersistenciaException extends PersistenciaException {
    private static final long serialVersionUID = 1L;

    public CargaPersistenciaException(Throwable causa) {
        super("Nao foi possivel carregar os dados do sistema.", causa);
    }
}
