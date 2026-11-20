package br.ufal.ic.p2.bikeshare.dominio;

import br.ufal.ic.p2.bikeshare.excecao.bicicleta.BicicletaException;

public class BicicletaConvencional extends Bicicleta {
    public BicicletaConvencional(String id, String estacao) {
        this(id, estacao, "disponivel");
    }

    public BicicletaConvencional(String id, String estacao, String estado) {
        super(id, estacao, estado);
    }

    @Override
    public String getTipo() {
        return "convencional";
    }

    @Override
    public boolean estaDisponivelParaAluguel() {
        return "disponivel".equals(getEstado());
    }

    @Override
    protected String getAtributoEspecifico(String atributo) {
        if ("nivelBateria".equals(atributo)) {
            throw new BicicletaException("Bicicleta nao possui bateria.");
        }
        throw atributoInexistente();
    }
}
