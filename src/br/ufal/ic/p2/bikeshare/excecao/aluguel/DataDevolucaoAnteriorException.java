package br.ufal.ic.p2.bikeshare.excecao.aluguel;

public class DataDevolucaoAnteriorException extends AluguelException {
    private static final long serialVersionUID = 1L;
    public DataDevolucaoAnteriorException() { super("Data de devolucao anterior a data de retirada."); }
}
