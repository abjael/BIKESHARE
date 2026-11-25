package br.ufal.ic.p2.bikeshare.excecao.usuario;
public class EmailNaoPodeSerNuloException extends UsuarioException {
    private static final long serialVersionUID = 1L;
    public EmailNaoPodeSerNuloException() { super("Email nao pode ser nulo."); }
}
