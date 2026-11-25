package br.ufal.ic.p2.bikeshare.excecao.aluguel;
public class IdentificacaoAluguelNulaException extends AluguelException {
    private static final long serialVersionUID = 1L;
    public IdentificacaoAluguelNulaException() { super("Identificacao do aluguel nao pode ser nula."); }
}
