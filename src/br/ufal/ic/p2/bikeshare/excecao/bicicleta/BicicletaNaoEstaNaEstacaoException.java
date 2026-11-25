package br.ufal.ic.p2.bikeshare.excecao.bicicleta;
import br.ufal.ic.p2.bikeshare.dominio.bicicleta.Bicicleta;
public class BicicletaNaoEstaNaEstacaoException extends BicicletaException {
    private static final long serialVersionUID = 1L;
    public BicicletaNaoEstaNaEstacaoException() { super("Bicicleta nao esta nesta estacao."); }
}
