package br.ufal.ic.p2.bikeshare.excecao.plano;
public class MensalidadeNaoNumericaException extends PlanoException {
    private static final long serialVersionUID = 1L;
    public MensalidadeNaoNumericaException() { super("Mensalidade deve ser numerica."); }
}
