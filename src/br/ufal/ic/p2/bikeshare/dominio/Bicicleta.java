package br.ufal.ic.p2.bikeshare.dominio;

import br.ufal.ic.p2.bikeshare.excecao.bicicleta.BicicletaException;

public abstract class Bicicleta {
    private final String id;
    private final String estacao;
    private final String estado;

    protected Bicicleta(String id, String estacao, String estado) {
        this.id = id;
        this.estacao = estacao;
        this.estado = estado;
    }

    public String getId() {
        return id;
    }

    public String getEstacao() {
        return estacao == null ? "nenhuma" : estacao;
    }

    public String getEstado() {
        return estado;
    }

    public abstract String getTipo();

    public abstract boolean estaDisponivelParaAluguel();

    public abstract void validarRetirada();

    public abstract Bicicleta retirarDaEstacao();

    public String getAtributo(String atributo) {
        if ("tipo".equals(atributo)) {
            return getTipo();
        }
        if ("estado".equals(atributo)) {
            return estado;
        }
        if ("estacao".equals(atributo)) {
            return getEstacao();
        }
        if (atributo == null || atributo.isEmpty()) {
            throw new BicicletaException("Atributo nao existe.");
        }
        return getAtributoEspecifico(atributo);
    }

    protected abstract String getAtributoEspecifico(String atributo);

    protected BicicletaException atributoInexistente() {
        return new BicicletaException("Atributo nao existe.");
    }
}
