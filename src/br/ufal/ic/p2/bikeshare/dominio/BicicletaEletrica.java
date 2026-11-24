package br.ufal.ic.p2.bikeshare.dominio;

import br.ufal.ic.p2.bikeshare.excecao.bicicleta.BicicletaException;

import java.math.BigDecimal;

public class BicicletaEletrica extends Bicicleta {
    private final int nivelBateria;

    public BicicletaEletrica(String id, String estacao, int nivelBateria) {
        this(id, estacao, DISPONIVEL, nivelBateria);
    }

    public BicicletaEletrica(String id, String estacao, String estado, int nivelBateria) {
        super(id, estacao, estado);
        this.nivelBateria = nivelBateria;
    }

    public int getNivelBateria() {
        return nivelBateria;
    }

    @Override
    public String getTipo() {
        return "eletrica";
    }

    @Override
    public BigDecimal getAdicionalPorMinuto() {
        return ConfiguracaoSistema.getAdicionalEletricaPorMinuto();
    }

    @Override
    public boolean estaDisponivelParaAluguel() {
        return super.estaDisponivelParaAluguel() && possuiBateriaSuficiente();
    }

    @Override
    protected void validarCondicoesDoTipo() {
        if (!possuiBateriaSuficiente()) {
            throw new BicicletaException("Nivel de bateria insuficiente.");
        }
    }

    private boolean possuiBateriaSuficiente() {
        return nivelBateria >= ConfiguracaoSistema.getNivelMinimoBateria();
    }

    @Override
    protected Bicicleta copiar(String novaEstacao, String novoEstado) {
        return new BicicletaEletrica(getId(), novaEstacao, novoEstado, nivelBateria);
    }

    @Override
    protected String getAtributoEspecifico(String atributo) {
        if ("nivelBateria".equals(atributo)) {
            return Integer.toString(nivelBateria);
        }
        throw atributoInexistente();
    }
}
