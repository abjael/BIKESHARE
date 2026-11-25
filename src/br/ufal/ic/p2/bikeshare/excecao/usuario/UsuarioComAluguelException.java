package br.ufal.ic.p2.bikeshare.excecao.usuario;

public class UsuarioComAluguelException extends UsuarioException {
    private static final long serialVersionUID = 1L;
    public UsuarioComAluguelException() { super("Nao e possivel remover usuario com aluguel em andamento."); }
}
