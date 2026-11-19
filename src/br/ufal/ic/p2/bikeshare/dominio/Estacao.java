package br.ufal.ic.p2.bikeshare.dominio;

import java.io.Serializable;

public class Estacao implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String id;
    private final String nome;
    private final String endereco;
    private final int capacidade;
    private int bicicletasDisponiveis;

    public Estacao(String id, String nome, String endereco, int capacidade) {
        this.id = id;
        this.nome = nome;
        this.endereco = endereco;
        this.capacidade = capacidade;
        this.bicicletasDisponiveis = 0;
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

    public int getVagasDisponiveis() {
        return capacidade - bicicletasDisponiveis;
    }

    public int getBicicletasDisponiveis() {
        return bicicletasDisponiveis;
    }
}
