package br.ufal.ic.p2.bikeshare.dominio;

import br.ufal.ic.p2.bikeshare.excecao.bicicleta.BicicletaException;

import java.math.BigDecimal;

public class BicicletaConvencional extends Bicicleta {
    public BicicletaConvencional(String id, String estacao) {
        this(id, estacao, DISPONIVEL);
    }

    public BicicletaConvencional(String id, String estacao, String estado) {
        super(id, estacao, estado);
    }

    @Override
    public String getTipo() {
        return "convencional";
    }

    @Override
    public BigDecimal getAdicionalPorMinuto() {
        return BigDecimal.ZERO;
    }

    @Override
    protected Bicicleta copiar(String novaEstacao, String novoEstado) {
        return new BicicletaConvencional(getId(), novaEstacao, novoEstado);
    }

    @Override
    protected String getAtributoEspecifico(String atributo) {
        if ("nivelBateria".equals(atributo)) {
            throw new BicicletaException("Bicicleta nao possui bateria.");
        }
        throw atributoInexistente();
    }
}
