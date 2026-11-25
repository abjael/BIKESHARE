package br.ufal.ic.p2.bikeshare.servico.usuario;

import br.ufal.ic.p2.bikeshare.servico.plano.FabricaPlano;
import br.ufal.ic.p2.bikeshare.servico.estado.EstadoSistema;
import br.ufal.ic.p2.bikeshare.servico.validacao.ValidadorEntrada;
import br.ufal.ic.p2.bikeshare.dominio.plano.Plano;
import br.ufal.ic.p2.bikeshare.dominio.pagamento.MetodoPagamento;
import br.ufal.ic.p2.bikeshare.dominio.pagamento.Cartao;
import br.ufal.ic.p2.bikeshare.dominio.pagamento.Pix;
import br.ufal.ic.p2.bikeshare.dominio.pagamento.SaldoPreCarregado;
import br.ufal.ic.p2.bikeshare.dominio.usuario.Usuario;
import br.ufal.ic.p2.bikeshare.excecao.usuario.UsuarioException;
import br.ufal.ic.p2.bikeshare.excecao.usuario.AtributoNaoPodeSerAlteradoException;
import br.ufal.ic.p2.bikeshare.excecao.usuario.EmailJaCadastradoException;
import br.ufal.ic.p2.bikeshare.excecao.usuario.ValidadeCartaoInvalidaException;
import br.ufal.ic.p2.bikeshare.excecao.usuario.ValorSaldoNuloException;
import br.ufal.ic.p2.bikeshare.excecao.usuario.ValorSaldoNaoNumericoException;
import br.ufal.ic.p2.bikeshare.excecao.usuario.ValorSaldoNaoPositivoException;
import br.ufal.ic.p2.bikeshare.excecao.pagamento.ValorMetodoPagamentoInvalidoException;
import br.ufal.ic.p2.bikeshare.excecao.pagamento.PagamentoException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.regex.Pattern;

import static br.ufal.ic.p2.bikeshare.servico.validacao.ValidadorEntrada.emBranco;
import static br.ufal.ic.p2.bikeshare.servico.usuario.ValidadorUsuario.validarPreenchimento;


final class ServicoAlteracaoUsuario {
    private static final Pattern VALIDADE_CARTAO = Pattern.compile("^(0[1-9]|1[0-2])/\\d{4}$");

    private final EstadoSistema estado;

    ServicoAlteracaoUsuario(EstadoSistema estado) {
        this.estado = estado;
    }

    Usuario alterarAtributo(Usuario usuario, String atributo, String valor) {
        if (atributo == null) {
            throw new AtributoNaoPodeSerAlteradoException();
        }
        if ("metodoPagamento".equals(atributo)) {
            return usuario.comMetodoPagamento(criarMetodoPagamentoSimples(usuario, valor));
        }
        return alterarDado(usuario, atributo, valor);
    }

    Usuario alterarComChave(Usuario usuario, String tipoPagamento, String chave) {
        return usuario.comMetodoPagamento(criarPix(tipoPagamento, chave));
    }

    Usuario alterarComCartao(Usuario usuario, String tipoPagamento, String numero, String validade) {
        return usuario.comMetodoPagamento(criarCartao(tipoPagamento, numero, validade));
    }

    private Usuario alterarDado(Usuario usuario, String atributo, String valor) {
        switch (atributo) {
            case "nome":
                return alterarNome(usuario, valor);
            case "email":
                return usuario.comEmail(validarEmail(usuario, valor));
            case "senha":
                return alterarSenha(usuario, valor);
            case "plano":
                return usuario.comPlano(alterarPlano(usuario.getPlano(), valor));
            case "saldo":
                return alterarSaldo(usuario, valor);
            default:
                throw new AtributoNaoPodeSerAlteradoException();
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
        boolean emailUsadoPorOutro = estado.buscarUsuarioPorEmail(emailNormalizado)
                .filter(existente -> !existente.getId().equals(usuario.getId())).isPresent();
        if (emailUsadoPorOutro) {
            throw new EmailJaCadastradoException();
        }
        return emailNormalizado;
    }

    private Plano alterarPlano(Plano planoAtual, String nomePlano) {
        if (planoAtual.getNome().equals(nomePlano)) {
            return planoAtual;
        }
        return FabricaPlano.criarComMensalidadePadrao(nomePlano);
    }

    private MetodoPagamento criarMetodoPagamentoSimples(Usuario usuario, String tipo) {
        if ("saldoPreCarregado".equals(tipo)) {
            return new SaldoPreCarregado(usuario.getSaldoPreCarregado());
        }
        throw metodoPagamentoInvalido();
    }

    private Cartao criarCartao(String tipo, String numero, String validade) {
        if (!"cartao".equals(tipo)) {
            throw metodoPagamentoInvalido();
        }
        validarPreenchimento(numero, "Numero do cartao nao pode ser nulo.");
        validarValidadeCartao(validade);
        return new Cartao(numero, validade);
    }

    private Pix criarPix(String tipo, String chave) {
        if (!"pix".equals(tipo) || chave == null) {
            throw metodoPagamentoInvalido();
        }
        validarPreenchimento(chave, "Chave pix nao pode ser nula.");
        return new Pix(chave);
    }

    private void validarValidadeCartao(String validade) {
        validarPreenchimento(validade, "Validade do cartao nao pode ser nula.");
        if (!VALIDADE_CARTAO.matcher(validade.trim()).matches()) {
            throw new ValidadeCartaoInvalidaException();
        }
    }

    private PagamentoException metodoPagamentoInvalido() {
        return new ValorMetodoPagamentoInvalidoException();
    }

    private Usuario alterarSaldo(Usuario usuario, String valor) {
        BigDecimal recarga = validarValorSaldo(valor);
        BigDecimal saldoAtualizado = usuario.getSaldoPreCarregado().add(recarga);
        return usuario.comSaldo(saldoAtualizado);
    }

    private BigDecimal validarValorSaldo(String valor) {
        if (emBranco(valor)) {
            throw new ValorSaldoNuloException();
        }
        BigDecimal valorNumerico = ValidadorEntrada.converterDecimal(valor)
                .orElseThrow(ValorSaldoNaoNumericoException::new);
        if (valorNumerico.signum() <= 0) {
            throw new ValorSaldoNaoPositivoException();
        }
        return valorNumerico.setScale(2, RoundingMode.HALF_UP);
    }
}
