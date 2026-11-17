package br.ufal.ic.p2.bikeshare.dominio;

public class Cartao extends MetodoPagamento {
    private static final long serialVersionUID = 1L;

    private final String numero;
    private final String validade;

    public Cartao(String numero, String validade) {
        this.numero = numero;
        this.validade = validade;
    }

    public String getNumero() {
        return numero;
    }

    public String getValidade() {
        return validade;
    }

    @Override
    public String getNome() {
        return "cartao";
    }

    @Override
    public String getAtributo(String atributo) {
        if ("numeroCartao".equals(atributo)) {
            return numero;
        }
        if ("validadeCartao".equals(atributo)) {
            return validade;
        }
        throw atributoNaoSuportado(atributo);
    }
}
