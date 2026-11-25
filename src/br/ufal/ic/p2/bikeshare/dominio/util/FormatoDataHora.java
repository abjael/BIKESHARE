package br.ufal.ic.p2.bikeshare.dominio.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.ResolverStyle;
import java.time.format.SignStyle;
import java.time.temporal.ChronoField;

public final class FormatoDataHora {
    private static final DateTimeFormatter ENTRADA = new DateTimeFormatterBuilder()
            .appendValue(ChronoField.DAY_OF_MONTH, 1, 2, SignStyle.NOT_NEGATIVE).appendLiteral('/')
            .appendValue(ChronoField.MONTH_OF_YEAR, 1, 2, SignStyle.NOT_NEGATIVE).appendLiteral('/')
            .appendValue(ChronoField.YEAR, 4).appendLiteral(' ')
            .appendValue(ChronoField.HOUR_OF_DAY, 1, 2, SignStyle.NOT_NEGATIVE).appendLiteral(':')
            .appendValue(ChronoField.MINUTE_OF_HOUR, 2)
            .toFormatter().withResolverStyle(ResolverStyle.STRICT);
    private static final DateTimeFormatter SAIDA = DateTimeFormatter.ofPattern("d/M/uuuu H:mm");

    private FormatoDataHora() {
    }

    public static LocalDateTime parse(String valor) {
        return LocalDateTime.parse(valor, ENTRADA);
    }

    public static String formatar(LocalDateTime valor) {
        return SAIDA.format(valor);
    }
}
