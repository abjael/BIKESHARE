package br.ufal.ic.p2.bikeshare.dominio.util;

import java.math.BigDecimal;


public final class ConfiguracaoSistema {
    private static final int nivelMinimoBateria = 15;
    private static final int minutosCarencia = 2;
    private static final int minutosMaximosAluguel = 1440;
    private static final int minutosFranquiaAssinante = 60;
    private static final BigDecimal tarifaPorMinuto = new BigDecimal("0.50");
    private static final BigDecimal tarifaExcedenteAssinante = new BigDecimal("0.30");
    private static final BigDecimal adicionalEletricaPorMinuto = new BigDecimal("0.20");
    private static final BigDecimal penalidadeAtraso = new BigDecimal("10.00");
    private static final BigDecimal mensalidadePadraoAssinante = new BigDecimal("30");

    private ConfiguracaoSistema() {
    }

    
    public static int getNivelMinimoBateria() {
        return nivelMinimoBateria;
    }

    public static int getMinutosCarencia() {
        return minutosCarencia;
    }

    public static int getMinutosMaximosAluguel() {
        return minutosMaximosAluguel;
    }

    public static int getMinutosFranquiaAssinante() {
        return minutosFranquiaAssinante;
    }

    public static BigDecimal getTarifaPorMinuto() {
        return tarifaPorMinuto;
    }

    public static BigDecimal getTarifaExcedenteAssinante() {
        return tarifaExcedenteAssinante;
    }

    public static BigDecimal getAdicionalEletricaPorMinuto() {
        return adicionalEletricaPorMinuto;
    }

    public static BigDecimal getPenalidadeAtraso() {
        return penalidadeAtraso;
    }

    public static BigDecimal getMensalidadePadraoAssinante() {
        return mensalidadePadraoAssinante;
    }

}
