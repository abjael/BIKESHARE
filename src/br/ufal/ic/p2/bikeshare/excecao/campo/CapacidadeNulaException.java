package br.ufal.ic.p2.bikeshare.excecao.campo;

public class CapacidadeNulaException extends CampoObrigatorioException {
    private static final long serialVersionUID = 1L;

    public CapacidadeNulaException() {
        super("Capacidade nao pode ser nula.");
    }
}
