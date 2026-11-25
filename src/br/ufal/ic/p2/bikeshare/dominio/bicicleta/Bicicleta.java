package br.ufal.ic.p2.bikeshare.dominio.bicicleta;

import br.ufal.ic.p2.bikeshare.dominio.bicicleta.EstadoBicicleta;
import br.ufal.ic.p2.bikeshare.excecao.bicicleta.BicicletaException;
import br.ufal.ic.p2.bikeshare.excecao.bicicleta.BicicletaNaoDisponivelException;
import br.ufal.ic.p2.bikeshare.excecao.bicicleta.AtributoBicicletaNaoExisteException;

import java.math.BigDecimal;


public abstract class Bicicleta {
    public static final String DISPONIVEL = "disponivel";
    public static final String EM_USO = "emUso";
    public static final String EM_MANUTENCAO = "emManutencao";

    private final String id;
    private final String estacao;
    private final EstadoBicicleta estado;

    protected Bicicleta(String id, String estacao, String estado) {
        if (id == null || id.trim().isEmpty()) {
            throw new br.ufal.ic.p2.bikeshare.excecao.bicicleta.IdentificacaoBicicletaNulaException();
        }
        EstadoBicicleta estadoValidado = EstadoBicicleta.fromRotulo(estado);
        this.id = id.trim();
        if (estadoValidado != EstadoBicicleta.EM_USO && (estacao == null || estacao.trim().isEmpty())) {
            throw new br.ufal.ic.p2.bikeshare.excecao.bicicleta.IdentificacaoBicicletaNulaException();
        }
        this.estacao = estacao == null ? null : estacao.trim();
        this.estado = estadoValidado;
    }

    public String getId() {
        return id;
    }

    public String getEstacao() {
        return estacao == null ? "nenhuma" : estacao;
    }

    public String getEstacaoPersistida() {
        return estacao;
    }

    public String getEstado() {
        return estado.getRotulo();
    }

    public boolean estaNaEstacao(String idEstacao) {
        return idEstacao.equals(getEstacao());
    }

    public abstract String getTipo();

    public abstract BigDecimal getAdicionalPorMinuto();

    
    public boolean estaDisponivelParaAluguel() {
        return estado == EstadoBicicleta.DISPONIVEL;
    }

    
    public void validarRetirada() {
        estado.exigirDisponivel();
        validarCondicoesDoTipo();
    }

    
    protected void validarCondicoesDoTipo() {
    }

    public Bicicleta retirarDaEstacao() {
        estado.exigirDisponivel();
        return copiar(null, EstadoBicicleta.EM_USO.getRotulo());
    }

    public Bicicleta devolverNaEstacao(String idEstacao) {
        exigirEmUso();
        validarEstacao(idEstacao);
        return copiar(idEstacao, EstadoBicicleta.DISPONIVEL.getRotulo());
    }

    public Bicicleta colocarEmManutencao(String idEstacao) {
        exigirEmUso();
        validarEstacao(idEstacao);
        return copiar(idEstacao, EstadoBicicleta.EM_MANUTENCAO.getRotulo());
    }

    private void exigirEmUso() {
        if (estado != EstadoBicicleta.EM_USO) {
            throw new br.ufal.ic.p2.bikeshare.excecao.bicicleta.BicicletaNaoDisponivelException();
        }
    }

    private void validarEstacao(String idEstacao) {
        if (idEstacao == null || idEstacao.trim().isEmpty()) {
            throw new br.ufal.ic.p2.bikeshare.excecao.bicicleta.IdentificacaoBicicletaNulaException();
        }
    }

    public String getAtributo(String atributo) {
        if ("tipo".equals(atributo)) {
            return getTipo();
        }
        if ("estado".equals(atributo)) {
            return estado.getRotulo();
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
        return new AtributoBicicletaNaoExisteException();
    }
}
