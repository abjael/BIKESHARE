package br.ufal.ic.p2.bikeshare.excecao.usuario;
public class CampoObrigatorioException extends UsuarioException {
    private static final long serialVersionUID = 1L;
    public CampoObrigatorioException(String mensagem) { super(mensagem); }
}
