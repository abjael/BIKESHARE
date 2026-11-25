package br.ufal.ic.p2.bikeshare.excecao.bicicleta;
public class NivelBateriaForaDaFaixaException extends BicicletaException {
    private static final long serialVersionUID = 1L;
    public NivelBateriaForaDaFaixaException() { super("Nivel de bateria deve estar entre 0 e 100."); }
}
