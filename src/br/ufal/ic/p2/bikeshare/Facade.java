package br.ufal.ic.p2.bikeshare;

import br.ufal.ic.p2.bikeshare.servico.SistemaBikeShare;

public class Facade {

    private final SistemaBikeShare sistema;

    public Facade() {
        sistema = new SistemaBikeShare();
    }

    public String criarUsuario(String nome, String email, String senha, String tipo, String cpfOuCnpj) {
        return sistema.criarPessoaFisica(nome, email, senha, tipo, cpfOuCnpj, "avulso", null);
    }

    public String criarUsuario(String nome, String email, String senha, String tipo, String cnpj, String razaoSocial) {
        return sistema.criarPessoaJuridica(nome, email, senha, tipo, cnpj, razaoSocial, "avulso", null);
    }

    public String criarUsuario(String nome, String email, String senha, String tipo, String cpf,
                                String plano, String mensalidade) {
        return sistema.criarPessoaFisica(nome, email, senha, tipo, cpf, plano, mensalidade);
    }

    public String criarUsuario(String nome, String email, String senha, String tipo, String cnpj,
                                String razaoSocial, String plano, String mensalidade) {
        return sistema.criarPessoaJuridica(nome, email, senha, tipo, cnpj, razaoSocial, plano, mensalidade);
    }

    public String getAtributoUsuario(String usuario, String atributo) {
        return sistema.getAtributoUsuario(usuario, atributo);
    }

    public void zerarSistema() {
        sistema.zerar();
    }

    public void encerrarSistema() {
        sistema.salvar();
    }
}
