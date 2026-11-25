package br.ufal.ic.p2.bikeshare.dominio.bicicleta;

import br.ufal.ic.p2.bikeshare.excecao.bicicleta.BicicletaNaoDisponivelException;
import br.ufal.ic.p2.bikeshare.excecao.bicicleta.EstadoBicicletaInvalidoException;

public enum EstadoBicicleta {
    DISPONIVEL("disponivel"),
    EM_USO("emUso"),
    EM_MANUTENCAO("emManutencao");

    private final String rotulo;

    EstadoBicicleta(String rotulo) {
        this.rotulo = rotulo;
    }

    public String getRotulo() {
        return rotulo;
    }

    public static EstadoBicicleta fromRotulo(String rotulo) {
        if (rotulo == null) {
            throw new EstadoBicicletaInvalidoException();
        }
        for (EstadoBicicleta estado : values()) {
            if (estado.rotulo.equals(rotulo)) {
                return estado;
            }
        }
        throw new EstadoBicicletaInvalidoException();
    }

    public void exigirDisponivel() {
        if (this != DISPONIVEL) {
            throw new BicicletaNaoDisponivelException();
        }
    }
}
