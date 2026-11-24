package br.ufal.ic.p2.bikeshare.dominio;

import java.math.BigDecimal;

public class PlanoAssinante implements Plano {
    private final BigDecimal mensalidade;

    public PlanoAssinante(BigDecimal mensalidade) {
        this.mensalidade = mensalidade;
    }

    public BigDecimal getMensalidade() {
        return mensalidade;
    }

    @Override
    public String getNome() {
        return "assinante";
    }

    @Override
    public String getMensalidadeFormatada() {
        String valor = mensalidade.stripTrailingZeros().toPlainString();
        int separadorDecimal = valor.indexOf('.');
        if (separadorDecimal < 0) {
            return valor + ",00";
        }
        if (valor.length() - separadorDecimal == 2) {
            valor += "0";
        }
        return valor.replace('.', ',');
    }

    @Override
    public BigDecimal calcularTarifa(long minutos) {
        long minutosExcedentes = Math.max(0, minutos - ConfiguracaoSistema.getMinutosFranquiaAssinante());
        return ConfiguracaoSistema.getTarifaExcedenteAssinante().multiply(BigDecimal.valueOf(minutosExcedentes));
    }
}
