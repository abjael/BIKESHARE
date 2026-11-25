package br.ufal.ic.p2.bikeshare.excecao.aluguel;

public class AluguelAindaEmAndamentoException extends AluguelException {
    private static final long serialVersionUID = 1L;
    public AluguelAindaEmAndamentoException() { super("Aluguel ainda esta em andamento."); }
}
