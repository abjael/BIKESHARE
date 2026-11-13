package br.ufal.ic.p2.bikeshare.dominio;

import br.ufal.ic.p2.bikeshare.excecao.usuario.UsuarioException;

public class PlanoAvulso implements Plano {
    private static final long serialVersionUID = 1L;

    @Override
    public String getNome() {
        return "avulso";
    }

    @Override
    public String getMensalidadeFormatada() {
        throw new UsuarioException("Plano nao possui mensalidade.");
    }
}
