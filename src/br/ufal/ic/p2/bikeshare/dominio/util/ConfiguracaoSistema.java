package br.ufal.ic.p2.bikeshare.dominio.util;

import java.math.BigDecimal;

public final class ConfiguracaoSistema {
    private static final int NIVEL_MINIMO_BATERIA = 15;
    private static final int MINUTOS_CARENCIA = 2;
    private static final int MINUTOS_MAXIMOS_ALUGUEL = 1440;
    private static final int MINUTOS_FRANQUIA_ASSINANTE = 60;
    private static final BigDecimal TARIFA_POR_MINUTO = new BigDecimal("0.50");
    private static final BigDecimal TARIFA_EXCEDENTE_ASSINANTE = new BigDecimal("0.30");
    private static final BigDecimal ADICIONAL_ELETRICA_POR_MINUTO = new BigDecimal("0.20");
    private static final BigDecimal PENALIDADE_ATRASO = new BigDecimal("10.00");
    private static final BigDecimal MENSALIDADE_PADRAO_ASSINANTE = new BigDecimal("30");

    private ConfiguracaoSistema() {
    }

    public static int getNivelMinimoBateria() {
        return NIVEL_MINIMO_BATERIA;
    }

    public static int getMinutosCarencia() {
        return MINUTOS_CARENCIA;
    }

    public static int getMinutosMaximosAluguel() {
        return MINUTOS_MAXIMOS_ALUGUEL;
    }

    public static int getMinutosFranquiaAssinante() {
        return MINUTOS_FRANQUIA_ASSINANTE;
    }

    public static BigDecimal getTarifaPorMinuto() {
        return TARIFA_POR_MINUTO;
    }

    public static BigDecimal getTarifaExcedenteAssinante() {
        return TARIFA_EXCEDENTE_ASSINANTE;
    }

    public static BigDecimal getAdicionalEletricaPorMinuto() {
        return ADICIONAL_ELETRICA_POR_MINUTO;
    }

    public static BigDecimal getPenalidadeAtraso() {
        return PENALIDADE_ATRASO;
    }

    public static BigDecimal getMensalidadePadraoAssinante() {
        return MENSALIDADE_PADRAO_ASSINANTE;
    }
}
