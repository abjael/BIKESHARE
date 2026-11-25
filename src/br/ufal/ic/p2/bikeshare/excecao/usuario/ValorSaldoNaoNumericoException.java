package br.ufal.ic.p2.bikeshare.excecao.usuario;

public class ValorSaldoNaoNumericoException extends UsuarioException {
    private static final long serialVersionUID = 1L;
    public ValorSaldoNaoNumericoException() { super("Valor deve ser numerico."); }
}
