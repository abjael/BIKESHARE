package br.ufal.ic.p2.bikeshare.excecao.usuario;
import br.ufal.ic.p2.bikeshare.dominio.usuario.Usuario;
public class UsuarioNaoEhPessoaJuridicaException extends UsuarioException {
    private static final long serialVersionUID = 1L;
    public UsuarioNaoEhPessoaJuridicaException() { super("Usuario nao eh pessoa juridica."); }
}
