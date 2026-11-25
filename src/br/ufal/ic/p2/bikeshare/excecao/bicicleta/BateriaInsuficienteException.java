package br.ufal.ic.p2.bikeshare.excecao.bicicleta;

public class BateriaInsuficienteException extends BicicletaException {
    private static final long serialVersionUID = 1L;
    public BateriaInsuficienteException() { super("Nivel de bateria insuficiente."); }
}
