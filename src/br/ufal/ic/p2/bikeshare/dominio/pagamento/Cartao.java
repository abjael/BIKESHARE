package br.ufal.ic.p2.bikeshare.dominio.pagamento;

import br.ufal.ic.p2.bikeshare.dominio.pagamento.MetodoPagamento;
import br.ufal.ic.p2.bikeshare.excecao.pagamento.CartaoVencidoException;
import br.ufal.ic.p2.bikeshare.excecao.pagamento.ValidadeCartaoInvalidaException;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Cartao extends MetodoPagamento {
    private final String numero;
    private final String validade;

    public Cartao(String numero, String validade) {
        if (numero == null || !numero.trim().matches("\\d{4}-\\d{4}-\\d{4}-\\d{4}")) {
            throw new br.ufal.ic.p2.bikeshare.excecao.pagamento.ValorMetodoPagamentoInvalidoException();
        }
        if (validade == null) {
            throw new ValidadeCartaoInvalidaException();
        }
        try {
            YearMonth.parse(validade, DateTimeFormatter.ofPattern("MM/yyyy"));
        } catch (DateTimeParseException excecao) {
            throw new ValidadeCartaoInvalidaException();
        }
        this.numero = numero.trim();
        this.validade = validade.trim();
    }

    public String getNumero() {
        return numero;
    }

    public String getValidade() {
        return validade;
    }

    @Override
    public String getNome() {
        return "cartao";
    }

    @Override
    public void validarPagamentoPendente() {
        validarPagamentoPendente(LocalDateTime.now());
    }

    @Override
    public void validarPagamentoPendente(LocalDateTime data) {
        YearMonth vencimento = YearMonth.parse(validade, DateTimeFormatter.ofPattern("MM/yyyy"));
        YearMonth mesDaRetirada = YearMonth.from(data);
        if (vencimento.isBefore(mesDaRetirada)) {
            throw new CartaoVencidoException();
        }
    }

    @Override
    public String getAtributo(String atributo) {
        if ("numeroCartao".equals(atributo)) {
            return numero;
        }
        if ("validadeCartao".equals(atributo)) {
            return validade;
        }
        throw atributoNaoSuportado(atributo);
    }
}
