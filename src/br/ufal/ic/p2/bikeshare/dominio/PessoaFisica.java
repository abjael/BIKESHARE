package br.ufal.ic.p2.bikeshare.dominio;

import br.ufal.ic.p2.bikeshare.excecao.usuario.UsuarioException;

public class PessoaFisica extends Usuario {
    private static final long serialVersionUID = 1L;
    private final String cpf;

    public PessoaFisica(String id, String nome, String email, String senha, String cpf, Plano plano) {
        super(id, nome, email, senha, plano);
        this.cpf = cpf;
    }

    public String getCpf() {
        return cpf;
    }

    @Override
    protected String getTipo() {
        return "pessoaFisica";
    }

    @Override
    protected String getAtributoEspecifico(String atributo) {
        if ("cpf".equals(atributo)) {
            return cpf;
        }
        if ("cnpj".equals(atributo) || "razaoSocial".equals(atributo)) {
            throw new UsuarioException("Usuario nao eh pessoa juridica.");
        }
        throw atributoInexistente();
    }
}
