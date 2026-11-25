package br.ufal.ic.p2.bikeshare.excecao.campo;

public class RazaoSocialNulaException extends CampoObrigatorioException {
    private static final long serialVersionUID = 1L;

    public RazaoSocialNulaException() {
        super("Razao social nao pode ser nula.");
    }
}
