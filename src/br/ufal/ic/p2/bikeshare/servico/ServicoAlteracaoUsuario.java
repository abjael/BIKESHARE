package br.ufal.ic.p2.bikeshare.servico;

import br.ufal.ic.p2.bikeshare.dominio.Cartao;
import br.ufal.ic.p2.bikeshare.dominio.MetodoPagamento;
import br.ufal.ic.p2.bikeshare.dominio.Pix;
import br.ufal.ic.p2.bikeshare.dominio.Plano;
import br.ufal.ic.p2.bikeshare.dominio.SaldoPreCarregado;
import br.ufal.ic.p2.bikeshare.dominio.Usuario;
import br.ufal.ic.p2.bikeshare.excecao.usuario.UsuarioException;

import java.math.BigDecimal;
import java.util.regex.Pattern;

import static br.ufal.ic.p2.bikeshare.servico.ValidadorEntrada.emBranco;
import static br.ufal.ic.p2.bikeshare.servico.ValidadorUsuario.validarPreenchimento;


final class ServicoAlteracaoUsuario {
    private static final Pattern VALIDADE_CARTAO = Pattern.compile("^(0[1-9]|1[0-2])/\\d{4}$");

    private final EstadoSistema estado;

    ServicoAlteracaoUsuario(EstadoSistema estado) {
        this.estado = estado;
    }

    Usuario alterar(Usuario usuario, String atributo, String valor, String tipoPagamento,
                    String numero, String validade, String chave) {
        if (atributo == null) {
            throw new UsuarioException("Atributo nao pode ser alterado.");
        }
        return alterarAtributo(usuario, atributo, valor, tipoPagamento, numero, validade, chave);
    }

    private Usuario alterarAtributo(Usuario usuario, String atributo, String valor, String tipoPagamento,
                                    String numero, String validade, String chave) {
        switch (atributo) {
            case "nome":
                return alterarNome(usuario, valor);
            case "email":
                return usuario.comEmail(validarEmail(usuario, valor));
            case "senha":
                return alterarSenha(usuario, valor);
            case "plano":
                return usuario.comPlano(alterarPlano(usuario.getPlano(), valor));
            case "metodoPagamento":
                return usuario.comMetodoPagamento(criarMetodoPagamento(usuario, tipoPagamento, numero, validade, chave));
            case "saldo":
                return alterarSaldo(usuario, valor);
            default:
                throw new UsuarioException("Atributo nao pode ser alterado.");
        }
    }

    private Usuario alterarNome(Usuario usuario, String nome) {
        validarPreenchimento(nome, "Nome nao pode ser nulo.");
        return usuario.comNome(nome);
    }

    private Usuario alterarSenha(Usuario usuario, String senha) {
        validarPreenchimento(senha, "Senha nao pode ser nula.");
        return usuario.comSenha(senha);
    }

    private String validarEmail(Usuario usuario, String email) {
        String emailNormalizado = ValidadorUsuario.validarEmail(email);
        Usuario existente = estado.buscarUsuarioPorEmail(emailNormalizado);
        if (existente != null && !existente.getId().equals(usuario.getId())) {
            throw new UsuarioException("Email ja cadastrado.");
        }
        return emailNormalizado;
    }

    private Plano alterarPlano(Plano planoAtual, String nomePlano) {
        if (planoAtual.getNome().equals(nomePlano)) {
            return planoAtual;
        }
        return FabricaPlano.criarComMensalidadePadrao(nomePlano);
    }

    private MetodoPagamento criarMetodoPagamento(Usuario usuario, String tipo, String numero,
                                                 String validade, String chave) {
        if ("cartao".equals(tipo)) {
            return criarCartao(numero, validade, chave);
        }
        if ("pix".equals(tipo)) {
            return criarPix(numero, validade, chave);
        }
        if ("saldoPreCarregado".equals(tipo) && emBranco(numero) && emBranco(validade) && emBranco(chave)) {
            return new SaldoPreCarregado(usuario.getSaldoPreCarregado());
        }
        throw metodoPagamentoInvalido();
    }

    private Cartao criarCartao(String numero, String validade, String chave) {
        if ((emBranco(numero) && emBranco(validade)) || !emBranco(chave)) {
            throw metodoPagamentoInvalido();
        }
        validarPreenchimento(numero, "Numero do cartao nao pode ser nulo.");
        validarValidadeCartao(validade);
        return new Cartao(numero, validade);
    }

    private Pix criarPix(String numero, String validade, String chave) {
        if (chave == null) {
            throw metodoPagamentoInvalido();
        }
        validarPreenchimento(chave, "Chave pix nao pode ser nula.");
        if (!emBranco(numero) || !emBranco(validade)) {
            throw metodoPagamentoInvalido();
        }
        return new Pix(chave);
    }

    private void validarValidadeCartao(String validade) {
        validarPreenchimento(validade, "Validade do cartao nao pode ser nula.");
        if (!VALIDADE_CARTAO.matcher(validade.trim()).matches()) {
            throw new UsuarioException("Validade do cartao invalida.");
        }
    }

    private UsuarioException metodoPagamentoInvalido() {
        return new UsuarioException("Valor de metodo de pagamento invalido.");
    }

    private Usuario alterarSaldo(Usuario usuario, String valor) {
        BigDecimal recarga = validarValorSaldo(valor);
        BigDecimal saldoAtualizado = usuario.getSaldoPreCarregado().add(recarga);
        return usuario.comSaldo(saldoAtualizado);
    }

    private BigDecimal validarValorSaldo(String valor) {
        if (emBranco(valor)) {
            throw new UsuarioException("Valor nao pode ser nulo.");
        }
        BigDecimal valorNumerico = ValidadorEntrada.converterDecimal(valor);
        if (valorNumerico == null) {
            throw new UsuarioException("Valor deve ser numerico.");
        }
        if (valorNumerico.signum() <= 0) {
            throw new UsuarioException("Valor deve ser positivo.");
        }
        return valorNumerico;
    }
}
