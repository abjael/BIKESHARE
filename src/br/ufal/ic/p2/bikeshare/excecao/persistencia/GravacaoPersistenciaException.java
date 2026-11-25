package br.ufal.ic.p2.bikeshare.excecao.persistencia;

public class GravacaoPersistenciaException extends PersistenciaException {
    private static final long serialVersionUID = 1L;

    public GravacaoPersistenciaException(Throwable causa) {
        super("Nao foi possivel salvar os dados do sistema.", causa);
    }
}
