package br.ufal.ic.p2.bikeshare.excecao.aluguel;
public class AtributoAluguelNaoExisteException extends AluguelException {
    private static final long serialVersionUID = 1L;
    public AtributoAluguelNaoExisteException() { super("Atributo nao existe."); }
}
