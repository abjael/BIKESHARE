package br.ufal.ic.p2.bikeshare.dominio.usuario;

import br.ufal.ic.p2.bikeshare.dominio.pagamento.MetodoPagamento;
import br.ufal.ic.p2.bikeshare.dominio.pagamento.SaldoPreCarregado;
import br.ufal.ic.p2.bikeshare.dominio.pagamento.SemMetodoPagamento;
import br.ufal.ic.p2.bikeshare.dominio.plano.Plano;
import br.ufal.ic.p2.bikeshare.dominio.util.FormatoMoeda;
import br.ufal.ic.p2.bikeshare.excecao.AtributoInexistenteException;
import br.ufal.ic.p2.bikeshare.excecao.BikeShareException;
import br.ufal.ic.p2.bikeshare.excecao.campo.NomeNuloException;
import br.ufal.ic.p2.bikeshare.excecao.campo.SenhaNulaException;
import br.ufal.ic.p2.bikeshare.excecao.pagamento.SaldoNegativoException;
import br.ufal.ic.p2.bikeshare.excecao.plano.PlanoInvalidoException;
import br.ufal.ic.p2.bikeshare.excecao.usuario.EmailInvalidoException;
import br.ufal.ic.p2.bikeshare.excecao.usuario.EmailNaoPodeSerNuloException;
import br.ufal.ic.p2.bikeshare.excecao.usuario.IdentificacaoUsuarioNulaException;
import br.ufal.ic.p2.bikeshare.excecao.usuario.PendenciaInvalidaException;
import br.ufal.ic.p2.bikeshare.excecao.usuario.UsuarioJaPossuiAluguelException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.regex.Pattern;

public abstract class Usuario {
    private static final Pattern EMAIL_VALIDO = Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");

    private final String id;
    private final String nome;
    private final String email;
    private final String senha;
    private final Plano plano;
    private final MetodoPagamento metodoPagamento;
    private final SaldoPreCarregado carteira;
    private final String aluguelAtivo;
    private final BigDecimal totalPendencias;

    protected Usuario(String id, String nome, String email, String senha, Plano plano) {
        this(id, nome, email, senha, plano, new SemMetodoPagamento(), BigDecimal.ZERO, null, BigDecimal.ZERO);
    }

    protected Usuario(String id, String nome, String email, String senha, Plano plano,
                      MetodoPagamento metodoPagamento, BigDecimal saldoPreCarregado, String aluguelAtivo,
                      BigDecimal totalPendencias) {
        if (id == null || id.trim().isEmpty()) {
            throw new IdentificacaoUsuarioNulaException();
        }
        if (emBranco(nome)) {
            throw new NomeNuloException();
        }
        String emailValidado = validarEmail(email);
        if (emBranco(senha)) {
            throw new SenhaNulaException();
        }
        if (plano == null) {
            throw new PlanoInvalidoException();
        }
        if (saldoPreCarregado == null || saldoPreCarregado.signum() < 0) {
            throw new SaldoNegativoException();
        }
        if (totalPendencias == null || totalPendencias.signum() < 0) {
            throw new PendenciaInvalidaException();
        }
        this.id = id;
        this.nome = nome.trim();
        this.email = emailValidado;
        this.senha = senha;
        this.plano = plano;
        this.metodoPagamento = metodoPagamento == null ? new SemMetodoPagamento() : metodoPagamento;
        this.carteira = this.metodoPagamento.getCarteira()
                .orElseGet(() -> new SaldoPreCarregado(saldoPreCarregado));
        this.aluguelAtivo = aluguelAtivo;
        this.totalPendencias = totalPendencias.setScale(2, RoundingMode.HALF_UP);
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getNome() {
        return nome;
    }

    public String getSenha() {
        return senha;
    }

    public Plano getPlano() {
        return plano;
    }

    public MetodoPagamento getMetodoPagamento() {
        return metodoPagamento;
    }

    public BigDecimal getSaldoPreCarregado() {
        return carteira.getSaldo();
    }

    public String getAluguelAtivo() {
        return aluguelAtivo;
    }

    public BigDecimal getTotalPendencias() {
        return totalPendencias;
    }

    public boolean possuiAluguelAtivo() {
        return aluguelAtivo != null;
    }

    public boolean estaInadimplente() {
        return totalPendencias.signum() > 0;
    }

    public Usuario comNome(String novoNome) {
        return criarCopia(dadosAtuais().comNome(novoNome));
    }

    public Usuario comEmail(String novoEmail) {
        return criarCopia(dadosAtuais().comEmail(novoEmail));
    }

    public Usuario comSenha(String novaSenha) {
        return criarCopia(dadosAtuais().comSenha(novaSenha));
    }

    public Usuario comPlano(Plano novoPlano) {
        return criarCopia(dadosAtuais().comPlano(novoPlano));
    }

    public Usuario comMetodoPagamento(MetodoPagamento novoMetodoPagamento) {
        return criarCopia(dadosAtuais().comMetodoPagamento(novoMetodoPagamento));
    }

    public Usuario comSaldo(BigDecimal novoSaldo) {
        MetodoPagamento pagamentoAtualizado = metodoPagamento.comSaldo(novoSaldo);
        return criarCopia(dadosAtuais().comCarteira(pagamentoAtualizado));
    }

    public Usuario comAluguelAtivo(String novoAluguelAtivo) {
        if (novoAluguelAtivo != null && aluguelAtivo != null) {
            throw new UsuarioJaPossuiAluguelException();
        }
        return criarCopia(dadosAtuais().comAluguelAtivo(novoAluguelAtivo));
    }

    public Usuario cobrar(BigDecimal valor) {
        MetodoPagamento pagamentoAtualizado = metodoPagamento.cobrar(valor);
        return criarCopia(dadosAtuais().comCarteira(pagamentoAtualizado));
    }

    public Usuario adicionarPendencia(BigDecimal valor) {
        if (valor == null || valor.signum() <= 0) {
            throw new PendenciaInvalidaException();
        }
        return criarCopia(dadosAtuais().comPendencias(totalPendencias.add(valor)
                .setScale(2, RoundingMode.HALF_UP)));
    }

        public void validarPagamentoPendente(LocalDateTime data) {
        metodoPagamento.validarPagamentoPendente(data);
    }

    /** Confere preenchimento e formato do email; devolve o email sem espacos nas pontas. */
    public static String validarEmail(String email) {
        if (emBranco(email)) {
            throw new EmailNaoPodeSerNuloException();
        }
        String emailNormalizado = email.trim();
        if (!EMAIL_VALIDO.matcher(emailNormalizado).matches()) {
            throw new EmailInvalidoException();
        }
        return emailNormalizado;
    }

    private static boolean emBranco(String valor) {
        return valor == null || valor.trim().isEmpty();
    }

    public String getAtributo(String atributo) {
        return getAtributoComum(atributo).or(() -> getAtributoPagamento(atributo))
                .orElseGet(() -> getAtributoEspecifico(atributo));
    }

    private Optional<String> getAtributoComum(String atributo) {
        if (atributo == null) {
            return Optional.empty();
        }
        return getAtributoCadastral(atributo).or(() -> getAtributoSituacao(atributo))
                .or(() -> getAtributoDoPlano(atributo));
    }

    private Optional<String> getAtributoCadastral(String atributo) {
        switch (atributo) {
            case "nome":
                return Optional.of(nome);
            case "email":
                return Optional.of(email);
            case "senha":
                return Optional.of(senha);
            case "tipo":
                return Optional.of(getTipo());
            default:
                return Optional.empty();
        }
    }

    private Optional<String> getAtributoSituacao(String atributo) {
        switch (atributo) {
            case "estado":
                return Optional.of(estaInadimplente() ? "inadimplente" : "ativo");
            case "aluguelAtivo":
                return Optional.of(possuiAluguelAtivo() ? aluguelAtivo : "nenhum");
            case "totalPendencias":
                return Optional.of(FormatoMoeda.formatar(totalPendencias));
            default:
                return Optional.empty();
        }
    }

    private Optional<String> getAtributoPagamento(String atributo) {
        if ("metodoPagamento".equals(atributo)) {
            return Optional.of(metodoPagamento.getNome());
        }
        if (!MetodoPagamento.ehAtributoDePagamento(atributo)) {
            return Optional.empty();
        }
        return Optional.of(metodoPagamento.getAtributo(atributo));
    }

    private Optional<String> getAtributoDoPlano(String atributo) {
        switch (atributo) {
            case "plano":
                return Optional.of(plano.getNome());
            case "mensalidade":
                return Optional.of(plano.getMensalidadeFormatada());
            default:
                return Optional.empty();
        }
    }

    protected abstract String getTipo();

    protected abstract String getAtributoEspecifico(String atributo);

    protected abstract Usuario criarCopia(DadosUsuario dados);

    private DadosUsuario dadosAtuais() {
        return new DadosUsuario(nome, email, senha, plano, metodoPagamento, carteira.getSaldo(), aluguelAtivo,
                totalPendencias);
    }

    /** Copia dos dados mutaveis do usuario; cada comX altera um campo de uma copia nova. */
    protected static final class DadosUsuario {
        String nome;
        String email;
        String senha;
        Plano plano;
        MetodoPagamento metodoPagamento;
        BigDecimal saldo;
        String aluguelAtivo;
        BigDecimal totalPendencias;

        DadosUsuario(String nome, String email, String senha, Plano plano, MetodoPagamento metodoPagamento,
                     BigDecimal saldo, String aluguelAtivo, BigDecimal totalPendencias) {
            this.nome = nome;
            this.email = email;
            this.senha = senha;
            this.plano = plano;
            this.metodoPagamento = metodoPagamento;
            this.saldo = saldo;
            this.aluguelAtivo = aluguelAtivo;
            this.totalPendencias = totalPendencias;
        }

        private DadosUsuario copia() {
            return new DadosUsuario(nome, email, senha, plano, metodoPagamento, saldo, aluguelAtivo,
                    totalPendencias);
        }

        DadosUsuario comNome(String valor) {
            DadosUsuario copia = copia();
            copia.nome = valor;
            return copia;
        }

        DadosUsuario comEmail(String valor) {
            DadosUsuario copia = copia();
            copia.email = valor;
            return copia;
        }

        DadosUsuario comSenha(String valor) {
            DadosUsuario copia = copia();
            copia.senha = valor;
            return copia;
        }

        DadosUsuario comPlano(Plano valor) {
            DadosUsuario copia = copia();
            copia.plano = valor;
            return copia;
        }

        DadosUsuario comMetodoPagamento(MetodoPagamento valor) {
            DadosUsuario copia = copia();
            copia.metodoPagamento = valor;
            return copia;
        }

        DadosUsuario comCarteira(MetodoPagamento pagamentoAtualizado) {
            DadosUsuario copia = comMetodoPagamento(pagamentoAtualizado);
            copia.saldo = pagamentoAtualizado.getSaldoOu(saldo);
            return copia;
        }

        DadosUsuario comAluguelAtivo(String valor) {
            DadosUsuario copia = copia();
            copia.aluguelAtivo = valor;
            return copia;
        }

        DadosUsuario comPendencias(BigDecimal valor) {
            DadosUsuario copia = copia();
            copia.totalPendencias = valor;
            return copia;
        }
    }

    protected BikeShareException atributoInexistente() {
        return new AtributoInexistenteException();
    }
}
