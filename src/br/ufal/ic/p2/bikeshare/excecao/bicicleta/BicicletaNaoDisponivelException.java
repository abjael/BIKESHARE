package br.ufal.ic.p2.bikeshare.excecao.bicicleta;

public class BicicletaNaoDisponivelException extends BicicletaException {
    private static final long serialVersionUID = 1L;
    public BicicletaNaoDisponivelException() { super("Bicicleta nao esta disponivel."); }
}
