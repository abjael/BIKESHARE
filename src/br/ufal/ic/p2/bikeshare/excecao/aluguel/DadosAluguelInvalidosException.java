package br.ufal.ic.p2.bikeshare.excecao.aluguel;

public class DadosAluguelInvalidosException extends AluguelException {
    private static final long serialVersionUID = 1L;

    public DadosAluguelInvalidosException() {
        super("Dados do aluguel invalidos.");
    }
}
