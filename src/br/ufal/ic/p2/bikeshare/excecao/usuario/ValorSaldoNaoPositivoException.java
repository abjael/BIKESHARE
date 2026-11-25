package br.ufal.ic.p2.bikeshare.excecao.usuario;

public class ValorSaldoNaoPositivoException extends UsuarioException {
    private static final long serialVersionUID = 1L;
    public ValorSaldoNaoPositivoException() { super("Valor deve ser positivo."); }
}
