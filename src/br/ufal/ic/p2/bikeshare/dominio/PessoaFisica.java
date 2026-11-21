package br.ufal.ic.p2.bikeshare.dominio;

import br.ufal.ic.p2.bikeshare.excecao.usuario.UsuarioException;

import java.math.BigDecimal;

public class PessoaFisica extends Usuario {
    private final String cpf;

    public PessoaFisica(String id, String nome, String email, String senha, String cpf, Plano plano) {
        super(id, nome, email, senha, plano);
        this.cpf = cpf;
    }

    public PessoaFisica(String id, String nome, String email, String senha, String cpf, Plano plano,
                        MetodoPagamento metodoPagamento, BigDecimal saldoPreCarregado) {
        this(id, nome, email, senha, cpf, plano, metodoPagamento, saldoPreCarregado, null);
    }

    public PessoaFisica(String id, String nome, String email, String senha, String cpf, Plano plano,
                        MetodoPagamento metodoPagamento, BigDecimal saldoPreCarregado, String aluguelAtivo) {
        this(id, nome, email, senha, cpf, plano, metodoPagamento, saldoPreCarregado, aluguelAtivo,
                BigDecimal.ZERO);
    }

    public PessoaFisica(String id, String nome, String email, String senha, String cpf, Plano plano,
                        MetodoPagamento metodoPagamento, BigDecimal saldoPreCarregado, String aluguelAtivo,
                        BigDecimal totalPendencias) {
        super(id, nome, email, senha, plano, metodoPagamento, saldoPreCarregado, aluguelAtivo, totalPendencias);
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

    @Override
    protected Usuario criarCopia(String nome, String email, String senha, Plano plano,
                                 MetodoPagamento metodoPagamento, BigDecimal saldoPreCarregado,
                                 String aluguelAtivo, BigDecimal totalPendencias) {
        return new PessoaFisica(getId(), nome, email, senha, cpf, plano, metodoPagamento,
                saldoPreCarregado, aluguelAtivo, totalPendencias);
    }
}
