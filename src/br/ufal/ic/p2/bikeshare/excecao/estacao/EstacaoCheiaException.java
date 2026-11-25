package br.ufal.ic.p2.bikeshare.excecao.estacao;

public class EstacaoCheiaException extends EstacaoException {
    private static final long serialVersionUID = 1L;
    public EstacaoCheiaException() { super("Estacao cheia."); }
}
