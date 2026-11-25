package br.ufal.ic.p2.bikeshare.excecao.plano;
public class MensalidadeNulaException extends PlanoException {
    private static final long serialVersionUID = 1L;
    public MensalidadeNulaException() { super("Mensalidade nao pode ser nula."); }
}
