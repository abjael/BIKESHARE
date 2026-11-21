package br.ufal.ic.p2.bikeshare.dominio;

import br.ufal.ic.p2.bikeshare.excecao.usuario.UsuarioException;

import java.math.BigDecimal;

public class PessoaJuridica extends Usuario {
    private final String cnpj;
    private final String razaoSocial;

    public PessoaJuridica(String id, String nome, String email, String senha, String cnpj,
                          String razaoSocial, Plano plano) {
        super(id, nome, email, senha, plano);
        this.cnpj = cnpj;
        this.razaoSocial = razaoSocial;
    }

    public PessoaJuridica(String id, String nome, String email, String senha, String cnpj,
                          String razaoSocial, Plano plano, MetodoPagamento metodoPagamento,
                          BigDecimal saldoPreCarregado) {
        this(id, nome, email, senha, cnpj, razaoSocial, plano, metodoPagamento, saldoPreCarregado, null);
    }

    public PessoaJuridica(String id, String nome, String email, String senha, String cnpj,
                          String razaoSocial, Plano plano, MetodoPagamento metodoPagamento,
                          BigDecimal saldoPreCarregado, String aluguelAtivo) {
        this(id, nome, email, senha, cnpj, razaoSocial, plano, metodoPagamento, saldoPreCarregado,
                aluguelAtivo, BigDecimal.ZERO);
    }

    public PessoaJuridica(String id, String nome, String email, String senha, String cnpj,
                          String razaoSocial, Plano plano, MetodoPagamento metodoPagamento,
                          BigDecimal saldoPreCarregado, String aluguelAtivo, BigDecimal totalPendencias) {
        super(id, nome, email, senha, plano, metodoPagamento, saldoPreCarregado, aluguelAtivo, totalPendencias);
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

    @Override
    protected Usuario criarCopia(String nome, String email, String senha, Plano plano,
                                 MetodoPagamento metodoPagamento, BigDecimal saldoPreCarregado,
                                 String aluguelAtivo, BigDecimal totalPendencias) {
        return new PessoaJuridica(getId(), nome, email, senha, cnpj, razaoSocial, plano,
                metodoPagamento, saldoPreCarregado, aluguelAtivo, totalPendencias);
    }
}
