package br.ufal.ic.p2.bikeshare.excecao.estacao;

public class EstacaoSemVagaException extends EstacaoException {
    private static final long serialVersionUID = 1L;
    public EstacaoSemVagaException() { super("Estacao sem vagas disponiveis."); }
}
