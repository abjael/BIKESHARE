package br.ufal.ic.p2.bikeshare.excecao.estacao;
public class CapacidadeNaoNumericaException extends EstacaoException {
    private static final long serialVersionUID = 1L;
    public CapacidadeNaoNumericaException() { super("Capacidade deve ser numerica."); }
}
