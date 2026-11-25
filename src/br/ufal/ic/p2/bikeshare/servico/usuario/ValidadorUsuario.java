package br.ufal.ic.p2.bikeshare.servico.usuario;

import br.ufal.ic.p2.bikeshare.dominio.usuario.Usuario;
import br.ufal.ic.p2.bikeshare.excecao.BikeShareException;
import br.ufal.ic.p2.bikeshare.servico.validacao.ValidadorEntrada;

import java.util.Locale;
import java.util.function.Supplier;

public final class ValidadorUsuario {
    private ValidadorUsuario() {
    }

    public static String validarEmail(String email) {
        return Usuario.validarEmail(email);
    }

    public static String normalizarEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }

    public static void validarPreenchimento(String valor, Supplier<? extends BikeShareException> erro) {
        ValidadorEntrada.exigirPreenchido(valor, erro);
    }
}
