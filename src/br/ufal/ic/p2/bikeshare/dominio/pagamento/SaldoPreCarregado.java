package br.ufal.ic.p2.bikeshare.dominio.pagamento;

import br.ufal.ic.p2.bikeshare.dominio.pagamento.MetodoPagamento;
import br.ufal.ic.p2.bikeshare.dominio.util.FormatoMoeda;
import br.ufal.ic.p2.bikeshare.excecao.pagamento.SaldoInsuficienteException;

import java.math.BigDecimal;
import java.util.Optional;

public class SaldoPreCarregado extends MetodoPagamento {
    private final BigDecimal saldo;

    public SaldoPreCarregado(BigDecimal saldo) {
        if (saldo == null || saldo.signum() < 0) {
            throw new br.ufal.ic.p2.bikeshare.excecao.usuario.ValorSaldoNaoPositivoException();
        }
        this.saldo = saldo.setScale(2, java.math.RoundingMode.HALF_UP);
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
            throw new SaldoInsuficienteException();
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
            throw new SaldoInsuficienteException();
        }
        return new SaldoPreCarregado(saldo.subtract(valor));
    }

    @Override
    public BigDecimal getSaldoOu(BigDecimal saldoAnterior) {
        return saldo;
    }

    @Override
    public Optional<SaldoPreCarregado> getCarteira() {
        return Optional.of(this);
    }
}
