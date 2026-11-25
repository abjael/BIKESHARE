package br.ufal.ic.p2.bikeshare.excecao.plano;
import br.ufal.ic.p2.bikeshare.dominio.plano.Plano;
public class PlanoSemMensalidadeException extends PlanoException {
    private static final long serialVersionUID = 1L;
    public PlanoSemMensalidadeException() { super("Plano nao possui mensalidade."); }
}
