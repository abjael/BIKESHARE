package br.ufal.ic.p2.bikeshare.servico;

import br.ufal.ic.p2.bikeshare.dominio.ConfiguracaoSistema;
import br.ufal.ic.p2.bikeshare.dominio.Plano;
import br.ufal.ic.p2.bikeshare.dominio.PlanoAssinante;
import br.ufal.ic.p2.bikeshare.dominio.PlanoAvulso;
import br.ufal.ic.p2.bikeshare.excecao.usuario.UsuarioException;

import java.math.BigDecimal;

import static br.ufal.ic.p2.bikeshare.servico.ValidadorEntrada.emBranco;


final class FabricaPlano {
    private static final String AVULSO = "avulso";
    private static final String ASSINANTE = "assinante";

    private FabricaPlano() {
    }

    
    static Plano criar(String nome, String mensalidade) {
        if (AVULSO.equals(nome)) {
            if (!emBranco(mensalidade)) {
                throw new UsuarioException("Plano nao possui mensalidade.");
            }
            return new PlanoAvulso();
        }
        if (!ASSINANTE.equals(nome)) {
            throw planoInvalido();
        }
        return new PlanoAssinante(validarMensalidade(mensalidade));
    }

    
    static Plano criarComMensalidadePadrao(String nome) {
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
            throw new UsuarioException("Mensalidade nao pode ser nula.");
        }
        BigDecimal mensalidade = ValidadorEntrada.converterDecimal(valor);
        if (mensalidade == null) {
            throw new UsuarioException("Mensalidade deve ser numerica.");
        }
        if (mensalidade.signum() < 0) {
            throw new UsuarioException("Mensalidade deve ser nao-negativa.");
        }
        return mensalidade;
    }

    private static UsuarioException planoInvalido() {
        return new UsuarioException("Plano invalido.");
    }
}
