package br.ufal.ic.p2.bikeshare.excecao.campo;

public class NumeroCartaoNuloException extends CampoObrigatorioException {
    private static final long serialVersionUID = 1L;

    public NumeroCartaoNuloException() {
        super("Numero do cartao nao pode ser nulo.");
    }
}
