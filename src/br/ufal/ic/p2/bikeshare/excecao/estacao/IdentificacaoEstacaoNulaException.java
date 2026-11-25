package br.ufal.ic.p2.bikeshare.excecao.estacao;

public class IdentificacaoEstacaoNulaException extends EstacaoException {
    private static final long serialVersionUID = 1L;
    public IdentificacaoEstacaoNulaException() { super("Identificacao da estacao nao pode ser nula."); }
}
