package br.ufal.ic.p2.bikeshare.excecao.persistencia;

public class DadosPersistidosInvalidosException extends PersistenciaException {
    private static final long serialVersionUID = 1L;

    public DadosPersistidosInvalidosException() {
        super("Formato de dados persistidos invalido.");
    }
}
