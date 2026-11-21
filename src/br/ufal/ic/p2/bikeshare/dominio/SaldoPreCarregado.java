package br.ufal.ic.p2.bikeshare.dominio;

import br.ufal.ic.p2.bikeshare.excecao.usuario.UsuarioException;

import java.math.BigDecimal;
import java.math.RoundingMode;

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
            return saldo.setScale(2, RoundingMode.HALF_UP).toPlainString().replace('.', ',');
        }
        throw atributoNaoSuportado(atributo);
    }

    @Override
    public MetodoPagamento comSaldo(BigDecimal novoSaldo) {
        return new SaldoPreCarregado(novoSaldo);
    }
}
