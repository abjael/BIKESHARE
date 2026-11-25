package br.ufal.ic.p2.bikeshare.dominio.pagamento;

import br.ufal.ic.p2.bikeshare.excecao.campo.ChavePixNulaException;

import java.time.LocalDateTime;

public class Pix extends MetodoPagamento {
    private final String chave;

    public Pix(String chave) {
        if (chave == null || chave.trim().isEmpty()) {
            throw new ChavePixNulaException();
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
    public void validarPagamentoPendente(LocalDateTime data) {
    }

    @Override
    public String getAtributo(String atributo) {
        if ("chavePix".equals(atributo)) {
            return chave;
        }
        throw atributoNaoSuportado(atributo);
    }
}
