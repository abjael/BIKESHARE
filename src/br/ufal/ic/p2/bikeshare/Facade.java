package br.ufal.ic.p2.bikeshare;

import br.ufal.ic.p2.bikeshare.servico.SistemaBikeShare;

public class Facade {
    private final SistemaBikeShare sistema = new SistemaBikeShare();

    public String criarUsuario(String nome, String email, String senha, String tipo, String cpfOuCnpj) {
        return sistema.executar(() -> sistema.usuarios().criarPessoaFisica(
                nome, email, senha, tipo, cpfOuCnpj, null, null));
    }

    public String criarUsuario(String nome, String email, String senha, String tipo, String cnpj,
                               String razaoSocial) {
        return sistema.executar(() -> sistema.usuarios().criarPessoaJuridica(
                nome, email, senha, tipo, cnpj, razaoSocial, null, null));
    }

    public String criarUsuario(String nome, String email, String senha, String tipo, String cpf,
                               String plano, String mensalidade) {
        return sistema.executar(() -> sistema.usuarios().criarPessoaFisica(
                nome, email, senha, tipo, cpf, plano, mensalidade));
    }

    public String criarUsuario(String nome, String email, String senha, String tipo, String cnpj,
                               String razaoSocial, String plano, String mensalidade) {
        return sistema.executar(() -> sistema.usuarios().criarPessoaJuridica(
                nome, email, senha, tipo, cnpj, razaoSocial, plano, mensalidade));
    }

    public String getAtributoUsuario(String usuario, String atributo) {
        return sistema.usuarios().getAtributo(usuario, atributo);
    }

    public String getUsuarioPorEmail(String email) {
        return sistema.usuarios().getIdPorEmail(email);
    }

    public void removerUsuario(String usuario) {
        sistema.executar(() -> sistema.usuarios().remover(usuario));
    }

    public void alterarUsuario(String usuario, String atributo, String valor) {
        sistema.executar(() -> sistema.usuarios().alterar(usuario, atributo, valor));
    }

    public void alterarUsuario(String usuario, String atributo, String valor1, String chave) {
        sistema.executar(() -> sistema.usuarios().alterarComChave(usuario, atributo, valor1, chave));
    }

    public void alterarUsuario(String usuario, String atributo, String valor1, String numero, String validade) {
        sistema.executar(() -> sistema.usuarios().alterarComCartao(usuario, atributo, valor1, numero, validade));
    }

    public void zerarSistema() {
        sistema.zerar();
    }

    public void encerrarSistema() {
        sistema.salvar();
    }

    public String criarEstacao(String nome, String endereco, String capacidade) {
        return sistema.executar(() -> sistema.estacoes().criar(nome, endereco, capacidade));
    }

    public String getVagasDisponiveis(String estacao) {
        return String.valueOf(sistema.estacoes().getVagasDisponiveis(estacao));
    }

    public String getBicicletasDisponiveis(String estacao) {
        return String.valueOf(sistema.bicicletas().getBicicletasDisponiveis(estacao));
    }

    public void adicionarBicicleta(String estacao, String bicicleta, String tipo) {
        sistema.executar(() -> sistema.bicicletas().adicionarBicicleta(estacao, bicicleta, tipo, null));
    }

    public void adicionarBicicleta(String estacao, String bicicleta, String tipo, String nivelBateria) {
        sistema.executar(() -> sistema.bicicletas().adicionarBicicleta(estacao, bicicleta, tipo, nivelBateria));
    }

    public void removerBicicleta(String estacao, String bicicleta) {
        sistema.executar(() -> sistema.bicicletas().removerBicicleta(estacao, bicicleta));
    }

    public String getAtributoBicicleta(String bicicleta, String atributo) {
        return sistema.bicicletas().getAtributoBicicleta(bicicleta, atributo);
    }

    public String getEstadoBicicleta(String bicicleta) {
        return sistema.bicicletas().getEstadoBicicleta(bicicleta);
    }

    public String retirarBicicleta(String usuario, String estacao, String bicicleta, String data) {
        return sistema.executar(() -> sistema.alugueis().retirarBicicleta(usuario, estacao, bicicleta, data));
    }

    public String getAtributoAluguel(String aluguel, String atributo) {
        return sistema.alugueis().getAtributo(aluguel, atributo);
    }

    public void devolverBicicleta(String aluguel, String estacao, String data) {
        sistema.executar(() -> sistema.alugueis().devolverBicicleta(aluguel, estacao, data));
    }
}
