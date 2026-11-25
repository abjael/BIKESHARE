package br.ufal.ic.p2.bikeshare.dominio.usuario;

import br.ufal.ic.p2.bikeshare.dominio.plano.Plano;
import br.ufal.ic.p2.bikeshare.dominio.pagamento.MetodoPagamento;
import br.ufal.ic.p2.bikeshare.dominio.usuario.Usuario;
import br.ufal.ic.p2.bikeshare.excecao.usuario.UsuarioNaoEhPessoaJuridicaException;

import java.math.BigDecimal;

public class PessoaFisica extends Usuario {
    private final String cpf;

    public PessoaFisica(String id, String nome, String email, String senha, String cpf, Plano plano) {
        super(id, nome, email, senha, plano);
        this.cpf = validarCpf(cpf);
    }

    public PessoaFisica(String id, String nome, String email, String senha, String cpf, Plano plano,
                        MetodoPagamento metodoPagamento, BigDecimal saldoPreCarregado, String aluguelAtivo,
                        BigDecimal totalPendencias) {
        super(id, nome, email, senha, plano, metodoPagamento, saldoPreCarregado, aluguelAtivo, totalPendencias);
        this.cpf = validarCpf(cpf);
    }

    public String getCpf() {
        return cpf;
    }

    private String validarCpf(String valor) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new br.ufal.ic.p2.bikeshare.excecao.usuario.CampoObrigatorioException("CPF nao pode ser nulo.");
        }
        return valor.trim();
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
            throw new UsuarioNaoEhPessoaJuridicaException();
        }
        throw atributoInexistente();
    }

    @Override
    protected Usuario criarCopia(DadosUsuario dados) {
        return new PessoaFisica(getId(), dados.nome, dados.email, dados.senha, cpf, dados.plano,
                dados.metodoPagamento, dados.saldo, dados.aluguelAtivo, dados.totalPendencias);
    }
}
