package br.ufal.ic.p2.bikeshare.servico.validacao;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.regex.Pattern;

public final class ValidadorEntrada {
    private static final Pattern INTEIRO = Pattern.compile("^-?\\d+$");
    private static final Pattern DECIMAL = Pattern.compile("^-?\\d+(?:[.,]\\d+)?$");
    private static final int BITS_INT = 31;

    private ValidadorEntrada() {
    }

    public static boolean emBranco(String valor) {
        return valor == null || valor.trim().isEmpty();
    }

    public static void exigirPreenchido(String valor, Supplier<? extends RuntimeException> excecao) {
        if (emBranco(valor)) {
            throw excecao.get();
        }
    }

    public static Optional<Integer> converterInteiro(String valor) {
        String texto = valor.trim();
        if (!INTEIRO.matcher(texto).matches()) {
            return Optional.empty();
        }
        BigInteger numero = new BigInteger(texto);
        return numero.bitLength() <= BITS_INT ? Optional.of(numero.intValue()) : Optional.empty();
    }

    public static Optional<BigDecimal> converterDecimal(String valor) {
        String texto = valor.trim();
        return DECIMAL.matcher(texto).matches()
                ? Optional.of(new BigDecimal(texto.replace(',', '.'))) : Optional.empty();
    }
}
