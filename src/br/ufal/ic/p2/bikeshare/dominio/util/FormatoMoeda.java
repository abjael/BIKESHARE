package br.ufal.ic.p2.bikeshare.dominio.util;

import java.math.BigDecimal;
import java.math.RoundingMode;


public final class FormatoMoeda {
    private FormatoMoeda() {
    }

    public static String formatar(BigDecimal valor) {
        return valor.setScale(2, RoundingMode.HALF_UP).toPlainString().replace('.', ',');
    }
}
