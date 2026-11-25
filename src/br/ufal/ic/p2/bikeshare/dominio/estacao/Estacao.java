package br.ufal.ic.p2.bikeshare.dominio.estacao;

import br.ufal.ic.p2.bikeshare.excecao.campo.EnderecoNuloException;
import br.ufal.ic.p2.bikeshare.excecao.campo.NomeNuloException;
import br.ufal.ic.p2.bikeshare.excecao.estacao.CapacidadeNaoPositivaException;
import br.ufal.ic.p2.bikeshare.excecao.estacao.IdentificacaoEstacaoNulaException;

public class Estacao {
    private final String id;
    private final String nome;
    private final String endereco;
    private final int capacidade;

    public Estacao(String id, String nome, String endereco, int capacidade) {
        if (id == null || id.trim().isEmpty()) {
            throw new IdentificacaoEstacaoNulaException();
        }
        if (nome == null || nome.trim().isEmpty()) {
            throw new NomeNuloException();
        }
        if (endereco == null || endereco.trim().isEmpty()) {
            throw new EnderecoNuloException();
        }
        if (capacidade <= 0) {
            throw new CapacidadeNaoPositivaException();
        }
        this.id = id.trim();
        this.nome = nome.trim();
        this.endereco = endereco.trim();
        this.capacidade = capacidade;
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

}
