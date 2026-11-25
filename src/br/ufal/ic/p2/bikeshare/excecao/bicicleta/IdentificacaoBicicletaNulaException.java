package br.ufal.ic.p2.bikeshare.excecao.bicicleta;
public class IdentificacaoBicicletaNulaException extends BicicletaException {
    private static final long serialVersionUID = 1L;
    public IdentificacaoBicicletaNulaException() { super("Identificacao da bicicleta nao pode ser nula."); }
}
