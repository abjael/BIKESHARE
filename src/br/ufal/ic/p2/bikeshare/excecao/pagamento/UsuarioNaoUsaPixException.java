package br.ufal.ic.p2.bikeshare.excecao.pagamento;
import br.ufal.ic.p2.bikeshare.dominio.usuario.Usuario;
public class UsuarioNaoUsaPixException extends PagamentoException {
    private static final long serialVersionUID = 1L;
    public UsuarioNaoUsaPixException() { super("Usuario nao usa pix."); }
}
