package br.ufal.ic.p2.bikeshare.excecao.estacao;

public class CapacidadeNaoPositivaException extends EstacaoException {
    private static final long serialVersionUID = 1L;
    public CapacidadeNaoPositivaException() { super("Capacidade deve ser positiva."); }
}
