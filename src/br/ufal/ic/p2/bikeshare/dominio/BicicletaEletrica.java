package br.ufal.ic.p2.bikeshare.dominio;

public class BicicletaEletrica extends Bicicleta {
    private final int nivelBateria;

    public BicicletaEletrica(String id, String estacao, int nivelBateria) {
        this(id, estacao, "disponivel", nivelBateria);
    }

    public BicicletaEletrica(String id, String estacao, String estado, int nivelBateria) {
        super(id, estacao, estado);
        this.nivelBateria = nivelBateria;
    }

    @Override
    public String getTipo() {
        return "eletrica";
    }

    @Override
    public boolean estaDisponivelParaAluguel() {
        return "disponivel".equals(getEstado()) && nivelBateria >= ConfiguracaoSistema.NIVEL_MINIMO_BATERIA;
    }

    @Override
    protected String getAtributoEspecifico(String atributo) {
        if ("nivelBateria".equals(atributo)) {
            return Integer.toString(nivelBateria);
        }
        throw atributoInexistente();
    }

    public int getNivelBateria() {
        return nivelBateria;
    }
}
