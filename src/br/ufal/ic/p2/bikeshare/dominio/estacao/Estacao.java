package br.ufal.ic.p2.bikeshare.dominio.estacao;


public class Estacao {
    private final String id;
    private final String nome;
    private final String endereco;
    private final int capacidade;

    public Estacao(String id, String nome, String endereco, int capacidade) {
        if (id == null || id.trim().isEmpty()) {
            throw new br.ufal.ic.p2.bikeshare.excecao.estacao.IdentificacaoEstacaoNulaException();
        }
        if (nome == null || nome.trim().isEmpty()) {
            throw new br.ufal.ic.p2.bikeshare.excecao.estacao.CampoObrigatorioEstacaoException("Nome nao pode ser nulo.");
        }
        if (endereco == null || endereco.trim().isEmpty()) {
            throw new br.ufal.ic.p2.bikeshare.excecao.estacao.CampoObrigatorioEstacaoException("Endereco nao pode ser nulo.");
        }
        if (capacidade <= 0) {
            throw new br.ufal.ic.p2.bikeshare.excecao.estacao.CapacidadeNaoPositivaException();
        }
        this.id = id.trim();
        this.nome = nome.trim();
        this.endereco = endereco.trim();
        this.capacidade = capacidade;
    }

    public Estacao(String id, String nome, String endereco, int capacidade, int quantidadeBicicletas) {
        this(id, nome, endereco, capacidade);
        if (quantidadeBicicletas < 0 || quantidadeBicicletas > capacidade) {
            throw new br.ufal.ic.p2.bikeshare.excecao.estacao.EstacaoSemVagaException();
        }
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
