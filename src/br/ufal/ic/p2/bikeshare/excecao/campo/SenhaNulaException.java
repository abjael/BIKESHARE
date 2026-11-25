package br.ufal.ic.p2.bikeshare.excecao.campo;

public class SenhaNulaException extends CampoObrigatorioException {
    private static final long serialVersionUID = 1L;

    public SenhaNulaException() {
        super("Senha nao pode ser nula.");
    }
}
