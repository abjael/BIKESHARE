package br.ufal.ic.p2.bikeshare.dominio;

public class Estacao {
    private final String id;
    private final String nome;
    private final String endereco;
    private final int capacidade;
    private final int quantidadeBicicletas;

    public Estacao(String id, String nome, String endereco, int capacidade) {
        this(id, nome, endereco, capacidade, 0);
    }

    public Estacao(String id, String nome, String endereco, int capacidade, int quantidadeBicicletas) {
        this.id = id;
        this.nome = nome;
        this.endereco = endereco;
        this.capacidade = capacidade;
        this.quantidadeBicicletas = quantidadeBicicletas;
    }

    public String getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public int getCapacidade() {
        return capacidade;
    }

    public int getQuantidadeBicicletas() {
        return quantidadeBicicletas;
    }

    public int getVagasDisponiveis() {
        return capacidade - quantidadeBicicletas;
    }

    public boolean possuiVagaDisponivel() {
        return quantidadeBicicletas < capacidade;
    }

    public Estacao adicionarBicicleta() {
        return new Estacao(id, nome, endereco, capacidade, quantidadeBicicletas + 1);
    }

    public Estacao removerBicicleta() {
        return new Estacao(id, nome, endereco, capacidade, quantidadeBicicletas - 1);
    }
}
