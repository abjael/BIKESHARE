package br.ufal.ic.p2.bikeshare.excecao.aluguel;

public class DataInvalidaException extends AluguelException {
    private static final long serialVersionUID = 1L;
    public DataInvalidaException() { super("Data invalida."); }
}
