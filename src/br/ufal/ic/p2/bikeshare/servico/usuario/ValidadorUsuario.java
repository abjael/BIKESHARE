package br.ufal.ic.p2.bikeshare.servico.usuario;

import br.ufal.ic.p2.bikeshare.servico.validacao.ValidadorEntrada;
import br.ufal.ic.p2.bikeshare.excecao.usuario.CampoObrigatorioException;
import br.ufal.ic.p2.bikeshare.excecao.usuario.EmailNaoPodeSerNuloException;
import br.ufal.ic.p2.bikeshare.excecao.usuario.EmailInvalidoException;

import java.util.Locale;
import java.util.regex.Pattern;

public final class ValidadorUsuario {
    private static final Pattern EMAIL_VALIDO = Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");

    private ValidadorUsuario() {
    }

    public static String validarEmail(String email) {
        if (ValidadorEntrada.emBranco(email)) {
            throw new EmailNaoPodeSerNuloException();
        }
        String emailNormalizado = email.trim();
        if (!EMAIL_VALIDO.matcher(emailNormalizado).matches()) {
            throw new EmailInvalidoException();
        }
        return emailNormalizado;
    }

    public static String normalizarEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }

    public static void validarPreenchimento(String valor, String mensagem) {
        ValidadorEntrada.exigirPreenchido(valor, () -> new CampoObrigatorioException(mensagem));
    }
}
