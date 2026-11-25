package br.ufal.ic.p2.bikeshare.excecao.plano;
public class MensalidadeNegativaException extends PlanoException {
    private static final long serialVersionUID = 1L;
    public MensalidadeNegativaException() { super("Mensalidade deve ser nao-negativa."); }
}
