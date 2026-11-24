package br.ufal.ic.p2.bikeshare.dominio;

import br.ufal.ic.p2.bikeshare.excecao.usuario.UsuarioException;

import java.math.BigDecimal;

public class SaldoPreCarregado extends MetodoPagamento {
    private final BigDecimal saldo;

    public SaldoPreCarregado(BigDecimal saldo) {
        this.saldo = saldo;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    @Override
    public String getNome() {
        return "saldoPreCarregado";
    }

    @Override
    public void validarPagamentoPendente() {
        if (saldo.signum() <= 0) {
            throw new UsuarioException("Saldo insuficiente.");
        }
    }

    @Override
    public String getAtributo(String atributo) {
        if ("saldo".equals(atributo)) {
            return FormatoMoeda.formatar(saldo);
        }
        throw atributoNaoSuportado(atributo);
    }

    @Override
    public MetodoPagamento comSaldo(BigDecimal novoSaldo) {
        return new SaldoPreCarregado(novoSaldo);
    }

    @Override
    public MetodoPagamento cobrar(BigDecimal valor) {
        if (saldo.compareTo(valor) < 0) {
            throw new UsuarioException("Saldo insuficiente.");
        }
        return new SaldoPreCarregado(saldo.subtract(valor));
    }

    @Override
    public BigDecimal getSaldoOu(BigDecimal saldoAnterior) {
        return saldo;
    }
}
