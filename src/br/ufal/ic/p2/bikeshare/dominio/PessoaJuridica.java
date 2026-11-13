package br.ufal.ic.p2.bikeshare.dominio;

import br.ufal.ic.p2.bikeshare.excecao.usuario.UsuarioException;

public class PessoaJuridica extends Usuario {
    private static final long serialVersionUID = 1L;
    private final String cnpj;
    private final String razaoSocial;

    public PessoaJuridica(String id, String nome, String email, String senha, String cnpj,
                          String razaoSocial, Plano plano) {
        super(id, nome, email, senha, plano);
        this.cnpj = cnpj;
        this.razaoSocial = razaoSocial;
    }

    public String getCnpj() {
        return cnpj;
    }

    public String getRazaoSocial() {
        return razaoSocial;
    }

    @Override
    protected String getTipo() {
        return "pessoaJuridica";
    }

    @Override
    protected String getAtributoEspecifico(String atributo) {
        if ("cnpj".equals(atributo)) {
            return cnpj;
        }
        if ("razaoSocial".equals(atributo)) {
            return razaoSocial;
        }
        if ("cpf".equals(atributo)) {
            throw new UsuarioException("Usuario nao eh pessoa fisica.");
        }
        throw atributoInexistente();
    }
}
