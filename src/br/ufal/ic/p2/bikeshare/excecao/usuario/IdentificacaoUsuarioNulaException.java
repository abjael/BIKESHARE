package br.ufal.ic.p2.bikeshare.excecao.usuario;

public class IdentificacaoUsuarioNulaException extends UsuarioException {
    private static final long serialVersionUID = 1L;
    public IdentificacaoUsuarioNulaException() { super("Identificacao do usuario nao pode ser nula."); }
}
