package br.ufal.ic.p2.bikeshare.dominio.pagamento;

import br.ufal.ic.p2.bikeshare.excecao.pagamento.MetodoPagamentoNaoCadastradoException;
import br.ufal.ic.p2.bikeshare.excecao.pagamento.UsuarioNaoUsaSaldoException;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class SemMetodoPagamento extends MetodoPagamento {
    @Override
    public String getNome() {
        return "nenhum";
    }

    @Override
    public String getAtributo(String atributo) {
        throw atributoNaoSuportado(atributo);
    }

    @Override
    public void validarPagamentoPendente(LocalDateTime data) {
        throw new MetodoPagamentoNaoCadastradoException();
    }

    @Override
    public MetodoPagamento comSaldo(BigDecimal saldo) {
        throw new UsuarioNaoUsaSaldoException();
    }
}
