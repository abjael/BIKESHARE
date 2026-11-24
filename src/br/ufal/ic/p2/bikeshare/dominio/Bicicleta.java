package br.ufal.ic.p2.bikeshare.dominio;

import br.ufal.ic.p2.bikeshare.excecao.bicicleta.BicicletaException;

import java.math.BigDecimal;


public abstract class Bicicleta {
    public static final String DISPONIVEL = "disponivel";
    public static final String EM_USO = "emUso";
    public static final String EM_MANUTENCAO = "emManutencao";

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

    public boolean estaNaEstacao(String idEstacao) {
        return idEstacao.equals(getEstacao());
    }

    public abstract String getTipo();

    public abstract BigDecimal getAdicionalPorMinuto();

    
    public boolean estaDisponivelParaAluguel() {
        return DISPONIVEL.equals(estado);
    }

    
    public void validarRetirada() {
        if (!DISPONIVEL.equals(estado)) {
            throw new BicicletaException("Bicicleta nao esta disponivel.");
        }
        validarCondicoesDoTipo();
    }

    
    protected void validarCondicoesDoTipo() {
    }

    public Bicicleta retirarDaEstacao() {
        return copiar(null, EM_USO);
    }

    public Bicicleta devolverNaEstacao(String idEstacao) {
        return copiar(idEstacao, DISPONIVEL);
    }

    public Bicicleta colocarEmManutencao(String idEstacao) {
        return copiar(idEstacao, EM_MANUTENCAO);
    }

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
            throw atributoInexistente();
        }
        return getAtributoEspecifico(atributo);
    }

    protected abstract Bicicleta copiar(String novaEstacao, String novoEstado);

    protected abstract String getAtributoEspecifico(String atributo);

    protected BicicletaException atributoInexistente() {
        return new BicicletaException("Atributo nao existe.");
    }
}
