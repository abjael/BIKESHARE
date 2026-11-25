package br.ufal.ic.p2.bikeshare.excecao.usuario;
import br.ufal.ic.p2.bikeshare.dominio.usuario.Usuario;
public class UsuarioInadimplenteException extends UsuarioException {
    private static final long serialVersionUID = 1L;
    public UsuarioInadimplenteException() { super("Usuario inadimplente."); }
}
