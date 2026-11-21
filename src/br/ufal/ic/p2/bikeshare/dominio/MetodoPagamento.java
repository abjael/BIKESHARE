package br.ufal.ic.p2.bikeshare.dominio;

import br.ufal.ic.p2.bikeshare.excecao.usuario.UsuarioException;

import java.math.BigDecimal;

public abstract class MetodoPagamento {
    public abstract String getNome();

    public abstract String getAtributo(String atributo);

    public abstract void validarPagamentoPendente();

    public MetodoPagamento cobrar(BigDecimal valor) {
        return this;
    }

    public BigDecimal getSaldoOu(BigDecimal saldoAnterior) {
        return saldoAnterior;
    }

    public MetodoPagamento comSaldo(BigDecimal saldo) {
        throw new UsuarioException("Usuario nao usa saldo pre-carregado.");
    }

    protected UsuarioException atributoNaoSuportado(String atributo) {
        if ("numeroCartao".equals(atributo) || "validadeCartao".equals(atributo)) {
            return new UsuarioException("Usuario nao usa cartao.");
        }
        if ("chavePix".equals(atributo)) {
            return new UsuarioException("Usuario nao usa pix.");
        }
        if ("saldo".equals(atributo)) {
            return new UsuarioException("Usuario nao usa saldo pre-carregado.");
        }
        return new UsuarioException("Atributo nao existe.");
    }
}
