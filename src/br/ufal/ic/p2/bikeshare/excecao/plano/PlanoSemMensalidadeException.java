package br.ufal.ic.p2.bikeshare.excecao.plano;

public class PlanoSemMensalidadeException extends PlanoException {
    private static final long serialVersionUID = 1L;
    public PlanoSemMensalidadeException() { super("Plano nao possui mensalidade."); }
}
