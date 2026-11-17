package br.ufal.ic.p2.bikeshare.servico;

import br.ufal.ic.p2.bikeshare.excecao.usuario.UsuarioException;

import java.util.Locale;
import java.util.regex.Pattern;

final class ValidadorUsuario {
    static final Pattern EMAIL_VALIDO = Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");
    static final Pattern VALOR_NUMERICO = Pattern.compile("^-?\\d+(?:[.,]\\d+)?$");

    private ValidadorUsuario() {
    }

    static String validarEmail(String email) {
        validarPreenchimento(email, "Email nao pode ser nulo.");
        String emailNormalizado = email.trim();
        if (!EMAIL_VALIDO.matcher(emailNormalizado).matches()) {
            throw new UsuarioException("Email invalido.");
        }
        return emailNormalizado;
    }

    static String normalizarEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }

    static void validarPreenchimento(String valor, String mensagem) {
        if (emBranco(valor)) {
            throw new UsuarioException(mensagem);
        }
    }

    static boolean emBranco(String valor) {
        return valor == null || valor.trim().isEmpty();
    }
}
