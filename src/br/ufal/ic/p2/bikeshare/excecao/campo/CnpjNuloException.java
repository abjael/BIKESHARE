package br.ufal.ic.p2.bikeshare.excecao.campo;

public class CnpjNuloException extends CampoObrigatorioException {
    private static final long serialVersionUID = 1L;

    public CnpjNuloException() {
        super("CNPJ nao pode ser nulo.");
    }
}
