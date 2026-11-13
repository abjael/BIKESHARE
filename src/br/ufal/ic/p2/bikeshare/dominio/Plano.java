package br.ufal.ic.p2.bikeshare.dominio;

import java.io.Serializable;

public interface Plano extends Serializable {
    String getNome();

    String getMensalidadeFormatada();
}
