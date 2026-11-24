package br.ufal.ic.p2.bikeshare.dominio;

import java.math.BigDecimal;


public final class ConfiguracaoSistema {
    private static int nivelMinimoBateria;
    private static int minutosCarencia;
    private static int minutosMaximosAluguel;
    private static int minutosFranquiaAssinante;
    private static BigDecimal tarifaPorMinuto;
    private static BigDecimal tarifaExcedenteAssinante;
    private static BigDecimal adicionalEletricaPorMinuto;
    private static BigDecimal penalidadeAtraso;
    private static BigDecimal mensalidadePadraoAssinante;

    static {
        restaurarPadroes();
    }

    private ConfiguracaoSistema() {
    }

    
    public static void restaurarPadroes() {
        nivelMinimoBateria = 15;
        minutosCarencia = 2;
        minutosMaximosAluguel = 1440;
        minutosFranquiaAssinante = 60;
        tarifaPorMinuto = new BigDecimal("0.50");
        tarifaExcedenteAssinante = new BigDecimal("0.30");
        adicionalEletricaPorMinuto = new BigDecimal("0.20");
        penalidadeAtraso = new BigDecimal("10.00");
        mensalidadePadraoAssinante = new BigDecimal("30");
    }

    public static int getNivelMinimoBateria() {
        return nivelMinimoBateria;
    }

    public static void setNivelMinimoBateria(int valor) {
        nivelMinimoBateria = valor;
    }

    public static int getMinutosCarencia() {
        return minutosCarencia;
    }

    public static void setMinutosCarencia(int valor) {
        minutosCarencia = valor;
    }

    public static int getMinutosMaximosAluguel() {
        return minutosMaximosAluguel;
    }

    public static void setMinutosMaximosAluguel(int valor) {
        minutosMaximosAluguel = valor;
    }

    public static int getMinutosFranquiaAssinante() {
        return minutosFranquiaAssinante;
    }

    public static void setMinutosFranquiaAssinante(int valor) {
        minutosFranquiaAssinante = valor;
    }

    public static BigDecimal getTarifaPorMinuto() {
        return tarifaPorMinuto;
    }

    public static void setTarifaPorMinuto(BigDecimal valor) {
        tarifaPorMinuto = valor;
    }

    public static BigDecimal getTarifaExcedenteAssinante() {
        return tarifaExcedenteAssinante;
    }

    public static void setTarifaExcedenteAssinante(BigDecimal valor) {
        tarifaExcedenteAssinante = valor;
    }

    public static BigDecimal getAdicionalEletricaPorMinuto() {
        return adicionalEletricaPorMinuto;
    }

    public static void setAdicionalEletricaPorMinuto(BigDecimal valor) {
        adicionalEletricaPorMinuto = valor;
    }

    public static BigDecimal getPenalidadeAtraso() {
        return penalidadeAtraso;
    }

    public static void setPenalidadeAtraso(BigDecimal valor) {
        penalidadeAtraso = valor;
    }

    public static BigDecimal getMensalidadePadraoAssinante() {
        return mensalidadePadraoAssinante;
    }

    public static void setMensalidadePadraoAssinante(BigDecimal valor) {
        mensalidadePadraoAssinante = valor;
    }
}
