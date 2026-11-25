package br.ufal.ic.p2.bikeshare.excecao.campo;

public class NomeNuloException extends CampoObrigatorioException {
    private static final long serialVersionUID = 1L;

    public NomeNuloException() {
        super("Nome nao pode ser nulo.");
    }
}
