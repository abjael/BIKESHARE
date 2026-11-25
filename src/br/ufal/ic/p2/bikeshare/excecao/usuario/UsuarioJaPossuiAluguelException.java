package br.ufal.ic.p2.bikeshare.excecao.usuario;

public class UsuarioJaPossuiAluguelException extends UsuarioException {
    private static final long serialVersionUID = 1L;
    public UsuarioJaPossuiAluguelException() { super("Usuario ja possui um aluguel em andamento."); }
}
