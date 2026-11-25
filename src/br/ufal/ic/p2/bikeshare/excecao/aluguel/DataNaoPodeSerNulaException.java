package br.ufal.ic.p2.bikeshare.excecao.aluguel;
public class DataNaoPodeSerNulaException extends AluguelException {
    private static final long serialVersionUID = 1L;
    public DataNaoPodeSerNulaException() { super("Data nao pode ser nula."); }
}
