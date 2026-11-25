package br.ufal.ic.p2.bikeshare.excecao.aluguel;

public class EstadoAluguelInvalidoException extends AluguelException {
    private static final long serialVersionUID = 1L;

    public EstadoAluguelInvalidoException() {
        super("Estado de aluguel invalido.");
    }
}
