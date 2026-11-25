package br.ufal.ic.p2.bikeshare.excecao.campo;

public class ValidadeCartaoNulaException extends CampoObrigatorioException {
    private static final long serialVersionUID = 1L;

    public ValidadeCartaoNulaException() {
        super("Validade do cartao nao pode ser nula.");
    }
}
