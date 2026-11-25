package br.ufal.ic.p2.bikeshare.excecao.campo;

public class CpfNuloException extends CampoObrigatorioException {
    private static final long serialVersionUID = 1L;

    public CpfNuloException() {
        super("CPF nao pode ser nulo.");
    }
}
