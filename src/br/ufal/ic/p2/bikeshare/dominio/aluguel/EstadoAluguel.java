package br.ufal.ic.p2.bikeshare.dominio.aluguel;

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
            throw new br.ufal.ic.p2.bikeshare.excecao.aluguel.AtributoAluguelNaoExisteException();
        }
        for (EstadoAluguel estado : values()) {
            if (estado.rotulo.equals(rotulo)) {
                return estado;
            }
        }
        throw new br.ufal.ic.p2.bikeshare.excecao.aluguel.AtributoAluguelNaoExisteException();
    }

    public void exigirEmAndamento() {
        if (this != EM_ANDAMENTO) {
            throw new br.ufal.ic.p2.bikeshare.excecao.aluguel.AluguelNaoEstaEmAndamentoException();
        }
    }
}
