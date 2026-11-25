package br.ufal.ic.p2.bikeshare.excecao.bicicleta;
public class AtributoBicicletaNaoExisteException extends BicicletaException {
    private static final long serialVersionUID = 1L;
    public AtributoBicicletaNaoExisteException() { super("Atributo nao existe."); }
}
