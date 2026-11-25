package br.ufal.ic.p2.bikeshare.excecao.usuario;

public class UsuarioNaoEhPessoaFisicaException extends UsuarioException {
    private static final long serialVersionUID = 1L;
    public UsuarioNaoEhPessoaFisicaException() { super("Usuario nao eh pessoa fisica."); }
}
