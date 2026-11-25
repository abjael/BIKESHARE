package br.ufal.ic.p2.bikeshare.excecao.estacao;
import br.ufal.ic.p2.bikeshare.dominio.estacao.Estacao;
public class EstacaoNaoExisteException extends EstacaoException {
    private static final long serialVersionUID = 1L;
    public EstacaoNaoExisteException() { super("Estacao nao existe."); }
}
