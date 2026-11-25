package br.ufal.ic.p2.bikeshare.excecao.usuario;
import br.ufal.ic.p2.bikeshare.dominio.usuario.Usuario;
public class UsuarioJaPossuiAluguelException extends UsuarioException {
    private static final long serialVersionUID = 1L;
    public UsuarioJaPossuiAluguelException() { super("Usuario ja possui um aluguel em andamento."); }
}
