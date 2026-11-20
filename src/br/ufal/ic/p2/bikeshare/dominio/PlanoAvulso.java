package br.ufal.ic.p2.bikeshare.dominio;

import br.ufal.ic.p2.bikeshare.excecao.usuario.UsuarioException;

public class PlanoAvulso implements Plano {
    @Override
    public String getNome() {
        return "avulso";
    }

    @Override
    public String getMensalidadeFormatada() {
        throw new UsuarioException("Plano nao possui mensalidade.");
    }
}
