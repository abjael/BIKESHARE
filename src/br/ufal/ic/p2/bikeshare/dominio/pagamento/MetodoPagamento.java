package br.ufal.ic.p2.bikeshare.dominio.pagamento;

import br.ufal.ic.p2.bikeshare.excecao.AtributoInexistenteException;
import br.ufal.ic.p2.bikeshare.excecao.BikeShareException;
import br.ufal.ic.p2.bikeshare.excecao.pagamento.UsuarioNaoUsaCartaoException;
import br.ufal.ic.p2.bikeshare.excecao.pagamento.UsuarioNaoUsaPixException;
import br.ufal.ic.p2.bikeshare.excecao.pagamento.UsuarioNaoUsaSaldoException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

public abstract class MetodoPagamento {
    public abstract String getNome();

    public abstract String getAtributo(String atributo);

    /** Confere se este metodo pode cobrar uma retirada feita em {@code data}. */
    public abstract void validarPagamentoPendente(LocalDateTime data);

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

    public static BikeShareException atributoNaoSuportado(String atributo) {
        if ("numeroCartao".equals(atributo) || "validadeCartao".equals(atributo)) {
            return new UsuarioNaoUsaCartaoException();
        }
        if ("chavePix".equals(atributo)) {
            return new UsuarioNaoUsaPixException();
        }
        if ("saldo".equals(atributo)) {
            return new UsuarioNaoUsaSaldoException();
        }
        return new AtributoInexistenteException();
    }

    public static boolean ehAtributoDePagamento(String atributo) {
        return "numeroCartao".equals(atributo) || "validadeCartao".equals(atributo)
                || "chavePix".equals(atributo) || "saldo".equals(atributo);
    }
}
