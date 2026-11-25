package br.ufal.ic.p2.bikeshare.servico.plano;

import br.ufal.ic.p2.bikeshare.dominio.plano.Plano;
import br.ufal.ic.p2.bikeshare.dominio.plano.PlanoAssinante;
import br.ufal.ic.p2.bikeshare.dominio.plano.PlanoAvulso;
import br.ufal.ic.p2.bikeshare.dominio.util.ConfiguracaoSistema;
import br.ufal.ic.p2.bikeshare.excecao.plano.MensalidadeNaoNumericaException;
import br.ufal.ic.p2.bikeshare.excecao.plano.MensalidadeNegativaException;
import br.ufal.ic.p2.bikeshare.excecao.plano.MensalidadeNulaException;
import br.ufal.ic.p2.bikeshare.excecao.plano.PlanoException;
import br.ufal.ic.p2.bikeshare.excecao.plano.PlanoInvalidoException;
import br.ufal.ic.p2.bikeshare.excecao.plano.PlanoSemMensalidadeException;
import br.ufal.ic.p2.bikeshare.servico.validacao.ValidadorEntrada;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static br.ufal.ic.p2.bikeshare.servico.validacao.ValidadorEntrada.emBranco;

public final class FabricaPlano {
    private static final String AVULSO = "avulso";
    private static final String ASSINANTE = "assinante";

    private FabricaPlano() {
    }

    public static Plano criar(String nome, String mensalidade) {
        if (nome == null) {
            nome = AVULSO;
        }
        if (AVULSO.equals(nome)) {
            if (!emBranco(mensalidade)) {
                throw new PlanoSemMensalidadeException();
            }
            return new PlanoAvulso();
        }
        if (!ASSINANTE.equals(nome)) {
            throw planoInvalido();
        }
        return new PlanoAssinante(validarMensalidade(mensalidade));
    }

    public static Plano criarComMensalidadePadrao(String nome) {
        if (nome == null) {
            nome = AVULSO;
        }
        if (AVULSO.equals(nome)) {
            return new PlanoAvulso();
        }
        if (ASSINANTE.equals(nome)) {
            return new PlanoAssinante(ConfiguracaoSistema.getMensalidadePadraoAssinante());
        }
        throw planoInvalido();
    }

    private static BigDecimal validarMensalidade(String valor) {
        if (emBranco(valor)) {
            throw new MensalidadeNulaException();
        }
        BigDecimal mensalidade = ValidadorEntrada.converterDecimal(valor)
                .orElseThrow(MensalidadeNaoNumericaException::new);
        if (mensalidade.signum() < 0) {
            throw new MensalidadeNegativaException();
        }
        return mensalidade.setScale(2, RoundingMode.HALF_UP);
    }

    private static PlanoException planoInvalido() {
        return new PlanoInvalidoException();
    }
}
