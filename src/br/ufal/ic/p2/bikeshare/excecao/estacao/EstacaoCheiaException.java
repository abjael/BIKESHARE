package br.ufal.ic.p2.bikeshare.excecao.estacao;
import br.ufal.ic.p2.bikeshare.dominio.estacao.Estacao;
public class EstacaoCheiaException extends EstacaoException {
    private static final long serialVersionUID = 1L;
    public EstacaoCheiaException() { super("Estacao cheia."); }
}
