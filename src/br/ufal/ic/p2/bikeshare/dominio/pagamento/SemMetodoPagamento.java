package br.ufal.ic.p2.bikeshare.dominio.pagamento;

import br.ufal.ic.p2.bikeshare.dominio.pagamento.MetodoPagamento;
import br.ufal.ic.p2.bikeshare.excecao.pagamento.MetodoPagamentoNaoCadastradoException;
import br.ufal.ic.p2.bikeshare.excecao.pagamento.UsuarioNaoUsaCartaoException;
import br.ufal.ic.p2.bikeshare.excecao.pagamento.UsuarioNaoUsaPixException;
import br.ufal.ic.p2.bikeshare.excecao.pagamento.UsuarioNaoUsaSaldoException;
import br.ufal.ic.p2.bikeshare.excecao.pagamento.AtributoPagamentoNaoExisteException;

import java.math.BigDecimal;

public class SemMetodoPagamento extends MetodoPagamento {
    @Override
    public String getNome() {
        return "nenhum";
    }

    @Override
    public String getAtributo(String atributo) {
        if ("numeroCartao".equals(atributo) || "validadeCartao".equals(atributo)) {
            throw new UsuarioNaoUsaCartaoException();
        }
        if ("chavePix".equals(atributo)) {
            throw new UsuarioNaoUsaPixException();
        }
        if ("saldo".equals(atributo)) {
            throw new UsuarioNaoUsaSaldoException();
        }
        throw new AtributoPagamentoNaoExisteException();
    }

    @Override
    public void validarPagamentoPendente() {
        throw new MetodoPagamentoNaoCadastradoException();
    }

    @Override
    public MetodoPagamento comSaldo(BigDecimal saldo) {
        throw new UsuarioNaoUsaSaldoException();
    }
}
