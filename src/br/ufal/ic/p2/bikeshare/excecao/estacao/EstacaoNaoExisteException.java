package br.ufal.ic.p2.bikeshare.excecao.estacao;

public class EstacaoNaoExisteException extends EstacaoException {
    private static final long serialVersionUID = 1L;
    public EstacaoNaoExisteException() { super("Estacao nao existe."); }
}
