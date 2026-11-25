package br.ufal.ic.p2.bikeshare.dominio.plano;

import br.ufal.ic.p2.bikeshare.dominio.util.ConfiguracaoSistema;
import br.ufal.ic.p2.bikeshare.dominio.util.FormatoMoeda;
import br.ufal.ic.p2.bikeshare.excecao.plano.MensalidadeNegativaException;
import br.ufal.ic.p2.bikeshare.excecao.plano.MensalidadeNulaException;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class PlanoAssinante implements Plano {
    private final BigDecimal mensalidade;

    public PlanoAssinante(BigDecimal mensalidade) {
        if (mensalidade == null) {
            throw new MensalidadeNulaException();
        }
        if (mensalidade.signum() < 0) {
            throw new MensalidadeNegativaException();
        }
        this.mensalidade = mensalidade.setScale(2, RoundingMode.HALF_UP);
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
        return FormatoMoeda.formatar(mensalidade);
    }

    @Override
    public BigDecimal calcularTarifa(long minutos) {
        long minutosExcedentes = Math.max(0, minutos - ConfiguracaoSistema.getMinutosFranquiaAssinante());
        return ConfiguracaoSistema.getTarifaExcedenteAssinante().multiply(BigDecimal.valueOf(minutosExcedentes));
    }
}
