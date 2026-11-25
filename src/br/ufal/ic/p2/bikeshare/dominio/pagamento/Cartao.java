package br.ufal.ic.p2.bikeshare.dominio.pagamento;

import br.ufal.ic.p2.bikeshare.excecao.campo.NumeroCartaoNuloException;
import br.ufal.ic.p2.bikeshare.excecao.campo.ValidadeCartaoNulaException;
import br.ufal.ic.p2.bikeshare.excecao.pagamento.CartaoVencidoException;
import br.ufal.ic.p2.bikeshare.excecao.pagamento.NumeroCartaoInvalidoException;
import br.ufal.ic.p2.bikeshare.excecao.pagamento.ValidadeCartaoInvalidaException;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;

public class Cartao extends MetodoPagamento {
    /** Digitos, podendo ter espacos ou hifens entre os grupos (ex.: 1111-2222-3333-4444). */
    private static final Pattern NUMERO_VALIDO = Pattern.compile("^\\d+(?:[ -]\\d+)*$");
    private static final DateTimeFormatter FORMATO_VALIDADE = DateTimeFormatter.ofPattern("MM/yyyy");

    private final String numero;
    private final String validade;

    public Cartao(String numero, String validade) {
        this.numero = validarNumero(numero);
        this.validade = validade == null ? null : validade.trim();
        lerValidade(validade);
    }

    private static String validarNumero(String numero) {
        if (numero == null || numero.trim().isEmpty()) {
            throw new NumeroCartaoNuloException();
        }
        if (!NUMERO_VALIDO.matcher(numero.trim()).matches()) {
            throw new NumeroCartaoInvalidoException();
        }
        return numero.trim();
    }

    private static YearMonth lerValidade(String validade) {
        if (validade == null || validade.trim().isEmpty()) {
            throw new ValidadeCartaoNulaException();
        }
        try {
            return YearMonth.parse(validade.trim(), FORMATO_VALIDADE);
        } catch (DateTimeParseException excecao) {
            throw new ValidadeCartaoInvalidaException();
        }
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
    public void validarPagamentoPendente(LocalDateTime data) {
        if (lerValidade(validade).isBefore(YearMonth.from(data))) {
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
