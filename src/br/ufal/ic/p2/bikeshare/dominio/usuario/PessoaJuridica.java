package br.ufal.ic.p2.bikeshare.dominio.usuario;

import br.ufal.ic.p2.bikeshare.dominio.plano.Plano;
import br.ufal.ic.p2.bikeshare.dominio.pagamento.MetodoPagamento;
import br.ufal.ic.p2.bikeshare.dominio.usuario.Usuario;
import br.ufal.ic.p2.bikeshare.excecao.usuario.UsuarioNaoEhPessoaFisicaException;

import java.math.BigDecimal;

public class PessoaJuridica extends Usuario {
    private final String cnpj;
    private final String razaoSocial;

    public PessoaJuridica(String id, String nome, String email, String senha, String cnpj,
                          String razaoSocial, Plano plano) {
        super(id, nome, email, senha, plano);
        this.cnpj = validarObrigatorio(cnpj, "CNPJ nao pode ser nulo.");
        this.razaoSocial = validarObrigatorio(razaoSocial, "Razao social nao pode ser nula.");
    }

    public PessoaJuridica(String id, String nome, String email, String senha, String cnpj,
                          String razaoSocial, Plano plano, MetodoPagamento metodoPagamento,
                          BigDecimal saldoPreCarregado, String aluguelAtivo, BigDecimal totalPendencias) {
        super(id, nome, email, senha, plano, metodoPagamento, saldoPreCarregado, aluguelAtivo, totalPendencias);
        this.cnpj = validarObrigatorio(cnpj, "CNPJ nao pode ser nulo.");
        this.razaoSocial = validarObrigatorio(razaoSocial, "Razao social nao pode ser nula.");
    }

    public String getCnpj() {
        return cnpj;
    }

    public String getRazaoSocial() {
        return razaoSocial;
    }

    private String validarObrigatorio(String valor, String mensagem) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new br.ufal.ic.p2.bikeshare.excecao.usuario.CampoObrigatorioException(mensagem);
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
