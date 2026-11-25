package br.ufal.ic.p2.bikeshare.dominio.usuario;

import br.ufal.ic.p2.bikeshare.dominio.pagamento.MetodoPagamento;
import br.ufal.ic.p2.bikeshare.dominio.plano.Plano;
import br.ufal.ic.p2.bikeshare.excecao.BikeShareException;
import br.ufal.ic.p2.bikeshare.excecao.campo.CnpjNuloException;
import br.ufal.ic.p2.bikeshare.excecao.campo.RazaoSocialNulaException;
import br.ufal.ic.p2.bikeshare.excecao.usuario.UsuarioNaoEhPessoaFisicaException;

import java.math.BigDecimal;
import java.util.function.Supplier;

public class PessoaJuridica extends Usuario {
    private final String cnpj;
    private final String razaoSocial;

    public PessoaJuridica(String id, String nome, String email, String senha, String cnpj,
                          String razaoSocial, Plano plano) {
        super(id, nome, email, senha, plano);
        this.cnpj = validarObrigatorio(cnpj, CnpjNuloException::new);
        this.razaoSocial = validarObrigatorio(razaoSocial, RazaoSocialNulaException::new);
    }

    public PessoaJuridica(String id, String nome, String email, String senha, String cnpj,
                          String razaoSocial, Plano plano, MetodoPagamento metodoPagamento,
                          BigDecimal saldoPreCarregado, String aluguelAtivo, BigDecimal totalPendencias) {
        super(id, nome, email, senha, plano, metodoPagamento, saldoPreCarregado, aluguelAtivo, totalPendencias);
        this.cnpj = validarObrigatorio(cnpj, CnpjNuloException::new);
        this.razaoSocial = validarObrigatorio(razaoSocial, RazaoSocialNulaException::new);
    }

    public String getCnpj() {
        return cnpj;
    }

    public String getRazaoSocial() {
        return razaoSocial;
    }

    private String validarObrigatorio(String valor, Supplier<? extends BikeShareException> erro) {
        if (valor == null || valor.trim().isEmpty()) {
            throw erro.get();
        }
        return valor.trim();
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
            throw new UsuarioNaoEhPessoaFisicaException();
        }
        throw atributoInexistente();
    }

    @Override
    protected Usuario criarCopia(DadosUsuario dados) {
        return new PessoaJuridica(getId(), dados.nome, dados.email, dados.senha, cnpj, razaoSocial, dados.plano,
                dados.metodoPagamento, dados.saldo, dados.aluguelAtivo, dados.totalPendencias);
    }
}
