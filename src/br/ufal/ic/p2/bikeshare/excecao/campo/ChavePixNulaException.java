package br.ufal.ic.p2.bikeshare.excecao.campo;

public class ChavePixNulaException extends CampoObrigatorioException {
    private static final long serialVersionUID = 1L;

    public ChavePixNulaException() {
        super("Chave pix nao pode ser nula.");
    }
}
