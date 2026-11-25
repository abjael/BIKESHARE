package br.ufal.ic.p2.bikeshare.dominio.plano;

import br.ufal.ic.p2.bikeshare.dominio.util.ConfiguracaoSistema;
import br.ufal.ic.p2.bikeshare.excecao.plano.PlanoSemMensalidadeException;

import java.math.BigDecimal;

public class PlanoAvulso implements Plano {
    @Override
    public String getNome() {
        return "avulso";
    }

    @Override
    public String getMensalidadeFormatada() {
        throw new PlanoSemMensalidadeException();
    }

    @Override
    public BigDecimal calcularTarifa(long minutos) {
        return ConfiguracaoSistema.getTarifaPorMinuto().multiply(BigDecimal.valueOf(minutos));
    }
}
