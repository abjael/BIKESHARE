package br.ufal.ic.p2.bikeshare.excecao.pagamento;
import br.ufal.ic.p2.bikeshare.dominio.usuario.Usuario;
public class UsuarioNaoUsaCartaoException extends PagamentoException {
    private static final long serialVersionUID = 1L;
    public UsuarioNaoUsaCartaoException() { super("Usuario nao usa cartao."); }
}
