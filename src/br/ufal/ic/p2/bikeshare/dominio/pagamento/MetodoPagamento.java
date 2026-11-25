package br.ufal.ic.p2.bikeshare.dominio.pagamento;

import br.ufal.ic.p2.bikeshare.dominio.pagamento.SaldoPreCarregado;
import br.ufal.ic.p2.bikeshare.excecao.pagamento.UsuarioNaoUsaSaldoException;
import br.ufal.ic.p2.bikeshare.excecao.pagamento.UsuarioNaoUsaCartaoException;
import br.ufal.ic.p2.bikeshare.excecao.pagamento.UsuarioNaoUsaPixException;
import br.ufal.ic.p2.bikeshare.excecao.pagamento.AtributoPagamentoNaoExisteException;
import br.ufal.ic.p2.bikeshare.excecao.pagamento.PagamentoException;
import java.time.LocalDateTime;

import java.math.BigDecimal;
import java.util.Optional;

public abstract class MetodoPagamento {
    public abstract String getNome();

    public abstract String getAtributo(String atributo);

    public abstract void validarPagamentoPendente();

    public void validarPagamentoPendente(LocalDateTime data) {
        validarPagamentoPendente();
    }

    public MetodoPagamento cobrar(BigDecimal valor) {
        return this;
    }

    public BigDecimal getSaldoOu(BigDecimal saldoAnterior) {
        return saldoAnterior;
    }

    public Optional<SaldoPreCarregado> getCarteira() {
        return Optional.empty();
    }

    public MetodoPagamento comSaldo(BigDecimal saldo) {
        throw new UsuarioNaoUsaSaldoException();
    }

    
    public static PagamentoException atributoNaoSuportado(String atributo) {
        if (ehAtributoDePagamento(atributo) && ("numeroCartao".equals(atributo)
                || "validadeCartao".equals(atributo))) {
            return new UsuarioNaoUsaCartaoException();
        }
        if (ehAtributoDePagamento(atributo) && "chavePix".equals(atributo)) {
            return new UsuarioNaoUsaPixException();
        }
        if (ehAtributoDePagamento(atributo) && "saldo".equals(atributo)) {
            return new UsuarioNaoUsaSaldoException();
        }
        return new AtributoPagamentoNaoExisteException();
    }

    public static boolean ehAtributoDePagamento(String atributo) {
        return "numeroCartao".equals(atributo) || "validadeCartao".equals(atributo)
                || "chavePix".equals(atributo) || "saldo".equals(atributo);
    }
}
