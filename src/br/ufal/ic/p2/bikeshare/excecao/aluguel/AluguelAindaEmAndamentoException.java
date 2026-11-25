package br.ufal.ic.p2.bikeshare.excecao.aluguel;
import br.ufal.ic.p2.bikeshare.dominio.aluguel.Aluguel;
public class AluguelAindaEmAndamentoException extends AluguelException {
    private static final long serialVersionUID = 1L;
    public AluguelAindaEmAndamentoException() { super("Aluguel ainda esta em andamento."); }
}
