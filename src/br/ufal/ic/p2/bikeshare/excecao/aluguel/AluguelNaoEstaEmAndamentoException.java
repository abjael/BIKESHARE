package br.ufal.ic.p2.bikeshare.excecao.aluguel;
import br.ufal.ic.p2.bikeshare.dominio.aluguel.Aluguel;
public class AluguelNaoEstaEmAndamentoException extends AluguelException {
    private static final long serialVersionUID = 1L;
    public AluguelNaoEstaEmAndamentoException() { super("Aluguel nao esta em andamento."); }
}
