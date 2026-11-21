package br.ufal.ic.p2.bikeshare.dominio;

import java.math.BigDecimal;

public final class ConfiguracaoSistema {
    public static final int NIVEL_MINIMO_BATERIA = 15;
    public static final int MINUTOS_CARENCIA = 2;
    public static final int MINUTOS_MAXIMOS_ALUGUEL = 1440;
    public static final int MINUTOS_FRANQUIA_ASSINANTE = 60;
    public static final BigDecimal TARIFA_POR_MINUTO = new BigDecimal("0.50");
    public static final BigDecimal TARIFA_EXCEDENTE_ASSINANTE = new BigDecimal("0.30");
    public static final BigDecimal ADICIONAL_ELETRICA_POR_MINUTO = new BigDecimal("0.20");
    public static final BigDecimal PENALIDADE_ATRASO = new BigDecimal("10.00");

    private ConfiguracaoSistema() {
    }
}
