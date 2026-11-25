package br.ufal.ic.p2.bikeshare.excecao.aluguel;

public class AluguelNaoExisteException extends AluguelException {
    private static final long serialVersionUID = 1L;
    public AluguelNaoExisteException() { super("Aluguel nao existe."); }
}
