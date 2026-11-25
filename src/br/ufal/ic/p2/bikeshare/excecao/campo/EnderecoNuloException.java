package br.ufal.ic.p2.bikeshare.excecao.campo;

public class EnderecoNuloException extends CampoObrigatorioException {
    private static final long serialVersionUID = 1L;

    public EnderecoNuloException() {
        super("Endereco nao pode ser nulo.");
    }
}
