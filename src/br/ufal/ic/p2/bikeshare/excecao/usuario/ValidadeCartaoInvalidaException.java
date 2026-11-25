package br.ufal.ic.p2.bikeshare.excecao.usuario;
public class ValidadeCartaoInvalidaException extends UsuarioException {
    private static final long serialVersionUID = 1L;
    public ValidadeCartaoInvalidaException() { super("Validade do cartao invalida."); }
}
