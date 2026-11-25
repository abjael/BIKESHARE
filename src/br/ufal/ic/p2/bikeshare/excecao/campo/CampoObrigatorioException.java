package br.ufal.ic.p2.bikeshare.excecao.campo;

import br.ufal.ic.p2.bikeshare.excecao.BikeShareException;

/** Categoria dos campos obrigatorios que chegaram nulos ou em branco; cada filha fixa a sua mensagem. */
public abstract class CampoObrigatorioException extends BikeShareException {
    private static final long serialVersionUID = 1L;

    protected CampoObrigatorioException(String mensagem) {
        super(mensagem);
    }
}
