package br.ufal.ic.p2.bikeshare.dominio.plano;

import java.math.BigDecimal;

public interface Plano {
    String getNome();

    String getMensalidadeFormatada();

    BigDecimal calcularTarifa(long minutos);
}
