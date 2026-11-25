package br.ufal.ic.p2.bikeshare.excecao.bicicleta;

public class NivelBateriaNaoNumericoException extends BicicletaException {
    private static final long serialVersionUID = 1L;
    public NivelBateriaNaoNumericoException() { super("Nivel de bateria deve ser numerico."); }
}
