package br.ufal.ic.p2.bikeshare.excecao.bicicleta;
public class TipoBicicletaInvalidoException extends BicicletaException {
    private static final long serialVersionUID = 1L;
    public TipoBicicletaInvalidoException() { super("Tipo invalido."); }
}
