package br.ufal.ic.p2.bikeshare.dominio;

import java.math.BigDecimal;

public class PlanoAssinante implements Plano {
    private static final long serialVersionUID = 1L;
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
}
