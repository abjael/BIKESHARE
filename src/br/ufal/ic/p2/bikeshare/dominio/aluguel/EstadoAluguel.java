package br.ufal.ic.p2.bikeshare.dominio.aluguel;

import br.ufal.ic.p2.bikeshare.excecao.aluguel.AluguelNaoEstaEmAndamentoException;
import br.ufal.ic.p2.bikeshare.excecao.aluguel.EstadoAluguelInvalidoException;

public enum EstadoAluguel {
    EM_ANDAMENTO("emAndamento"),
    FINALIZADO("finalizado"),
    CANCELADO("cancelado"),
    EXPIRADO("expirado");

    private final String rotulo;

    EstadoAluguel(String rotulo) {
        this.rotulo = rotulo;
    }

    public String getRotulo() {
        return rotulo;
    }

    public static EstadoAluguel fromRotulo(String rotulo) {
        if (rotulo == null) {
            throw new EstadoAluguelInvalidoException();
        }
        for (EstadoAluguel estado : values()) {
            if (estado.rotulo.equals(rotulo)) {
                return estado;
            }
        }
        throw new EstadoAluguelInvalidoException();
    }

    public void exigirEmAndamento() {
        if (this != EM_ANDAMENTO) {
            throw new AluguelNaoEstaEmAndamentoException();
        }
    }
}
