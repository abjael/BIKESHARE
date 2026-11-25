package br.ufal.ic.p2.bikeshare.dominio.pagamento;

import br.ufal.ic.p2.bikeshare.dominio.pagamento.MetodoPagamento;
public class Pix extends MetodoPagamento {
    private final String chave;

    public Pix(String chave) {
        if (chave == null || chave.trim().isEmpty()) {
            throw new br.ufal.ic.p2.bikeshare.excecao.pagamento.ValorMetodoPagamentoInvalidoException();
        }
        this.chave = chave.trim();
    }

    public String getChave() {
        return chave;
    }

    @Override
    public String getNome() {
        return "pix";
    }

    @Override
    public void validarPagamentoPendente() {
    }

    @Override
    public String getAtributo(String atributo) {
        if ("chavePix".equals(atributo)) {
            return chave;
        }
        throw atributoNaoSuportado(atributo);
    }
}
