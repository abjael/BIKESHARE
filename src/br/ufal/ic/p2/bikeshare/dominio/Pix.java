package br.ufal.ic.p2.bikeshare.dominio;

public class Pix extends MetodoPagamento {
    private static final long serialVersionUID = 1L;

    private final String chave;

    public Pix(String chave) {
        this.chave = chave;
    }

    public String getChave() {
        return chave;
    }

    @Override
    public String getNome() {
        return "pix";
    }

    @Override
    public String getAtributo(String atributo) {
        if ("chavePix".equals(atributo)) {
            return chave;
        }
        throw atributoNaoSuportado(atributo);
    }
}
