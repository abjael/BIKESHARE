package br.ufal.ic.p2.bikeshare.servico;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.regex.Pattern;


final class ValidadorEntrada {
    private static final Pattern INTEIRO = Pattern.compile("^-?\\d+$");
    private static final Pattern DECIMAL = Pattern.compile("^-?\\d+(?:[.,]\\d+)?$");
    private static final int BITS_INT = 31;

    private ValidadorEntrada() {
    }

    static boolean emBranco(String valor) {
        return valor == null || valor.trim().isEmpty();
    }

    static Integer converterInteiro(String valor) {
        String texto = valor.trim();
        if (!INTEIRO.matcher(texto).matches()) {
            return null;
        }
        BigInteger numero = new BigInteger(texto);
        return numero.bitLength() <= BITS_INT ? numero.intValue() : null;
    }

    static BigDecimal converterDecimal(String valor) {
        String texto = valor.trim();
        return DECIMAL.matcher(texto).matches() ? new BigDecimal(texto.replace(',', '.')) : null;
    }
}
