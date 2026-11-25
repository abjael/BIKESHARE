package br.ufal.ic.p2.bikeshare.excecao.usuario;
public class ValorSaldoNuloException extends UsuarioException {
    private static final long serialVersionUID = 1L;
    public ValorSaldoNuloException() { super("Valor nao pode ser nulo."); }
}
