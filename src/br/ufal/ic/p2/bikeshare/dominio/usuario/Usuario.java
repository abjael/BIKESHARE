package br.ufal.ic.p2.bikeshare.dominio.usuario;

import br.ufal.ic.p2.bikeshare.dominio.plano.Plano;
import br.ufal.ic.p2.bikeshare.dominio.pagamento.MetodoPagamento;
import br.ufal.ic.p2.bikeshare.dominio.pagamento.SaldoPreCarregado;
import br.ufal.ic.p2.bikeshare.dominio.pagamento.SemMetodoPagamento;
import br.ufal.ic.p2.bikeshare.dominio.util.FormatoMoeda;
import br.ufal.ic.p2.bikeshare.excecao.usuario.UsuarioException;
import br.ufal.ic.p2.bikeshare.excecao.usuario.AtributoUsuarioNaoExisteException;
import br.ufal.ic.p2.bikeshare.excecao.pagamento.MetodoPagamentoNaoCadastradoException;
import br.ufal.ic.p2.bikeshare.excecao.pagamento.UsuarioNaoUsaSaldoException;
import java.time.LocalDateTime;

import java.math.BigDecimal;
import java.util.Optional;


public abstract class Usuario {
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
            throw new br.ufal.ic.p2.bikeshare.excecao.usuario.IdentificacaoUsuarioNulaException();
        }
        if (nome == null || nome.trim().isEmpty()) {
            throw new br.ufal.ic.p2.bikeshare.excecao.usuario.CampoObrigatorioException("Nome nao pode ser nulo.");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new br.ufal.ic.p2.bikeshare.excecao.usuario.EmailNaoPodeSerNuloException();
        }
        if (senha == null || senha.trim().isEmpty()) {
            throw new br.ufal.ic.p2.bikeshare.excecao.usuario.CampoObrigatorioException("Senha nao pode ser nula.");
        }
        if (plano == null) {
            throw new br.ufal.ic.p2.bikeshare.excecao.plano.PlanoInvalidoException();
        }
        if (saldoPreCarregado == null || saldoPreCarregado.signum() < 0
                || totalPendencias == null || totalPendencias.signum() < 0) {
            throw new br.ufal.ic.p2.bikeshare.excecao.usuario.ValorSaldoNaoPositivoException();
        }
        this.id = id;
        this.nome = nome.trim();
        this.email = email.trim();
        this.senha = senha;
        this.plano = plano;
        this.metodoPagamento = metodoPagamento == null ? new SemMetodoPagamento() : metodoPagamento;
        this.carteira = this.metodoPagamento.getCarteira()
                .orElseGet(() -> new SaldoPreCarregado(saldoPreCarregado));
        this.aluguelAtivo = aluguelAtivo;
        this.totalPendencias = totalPendencias.setScale(2, java.math.RoundingMode.HALF_UP);
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
        if (metodoPagamento == null) {
            throw new UsuarioNaoUsaSaldoException();
        }
        MetodoPagamento pagamentoAtualizado = metodoPagamento.comSaldo(novoSaldo);
        return criarCopia(dadosAtuais().comCarteira(pagamentoAtualizado));
    }

    public Usuario comAluguelAtivo(String novoAluguelAtivo) {
        if (novoAluguelAtivo != null && aluguelAtivo != null) {
            throw new br.ufal.ic.p2.bikeshare.excecao.usuario.UsuarioJaPossuiAluguelException();
        }
        return criarCopia(dadosAtuais().comAluguelAtivo(novoAluguelAtivo));
    }

    public Usuario cobrar(BigDecimal valor) {
        MetodoPagamento pagamentoAtualizado = metodoPagamento.cobrar(valor);
        return criarCopia(dadosAtuais().comCarteira(pagamentoAtualizado));
    }

    public Usuario adicionarPendencia(BigDecimal valor) {
        if (valor == null || valor.signum() <= 0) {
            throw new br.ufal.ic.p2.bikeshare.excecao.pagamento.ValorMetodoPagamentoInvalidoException();
        }
        return criarCopia(dadosAtuais().comPendencias(totalPendencias.add(valor)
                .setScale(2, java.math.RoundingMode.HALF_UP)));
    }

    
    public void validarPagamentoPendente() {
        validarPagamentoPendente(LocalDateTime.now());
    }

    public void validarPagamentoPendente(LocalDateTime data) {
        if (metodoPagamento == null) {
            throw new MetodoPagamentoNaoCadastradoException();
        }
        metodoPagamento.validarPagamentoPendente(data);
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

    protected static final class DadosUsuario {
        final String nome;
        final String email;
        final String senha;
        final Plano plano;
        final MetodoPagamento metodoPagamento;
        final BigDecimal saldo;
        final String aluguelAtivo;
        final BigDecimal totalPendencias;

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

        DadosUsuario comNome(String valor) {
            return new DadosUsuario(valor, email, senha, plano, metodoPagamento, saldo, aluguelAtivo,
                    totalPendencias);
        }

        DadosUsuario comEmail(String valor) {
            return new DadosUsuario(nome, valor, senha, plano, metodoPagamento, saldo, aluguelAtivo,
                    totalPendencias);
        }

        DadosUsuario comSenha(String valor) {
            return new DadosUsuario(nome, email, valor, plano, metodoPagamento, saldo, aluguelAtivo,
                    totalPendencias);
        }

        DadosUsuario comPlano(Plano valor) {
            return new DadosUsuario(nome, email, senha, valor, metodoPagamento, saldo, aluguelAtivo,
                    totalPendencias);
        }

        DadosUsuario comMetodoPagamento(MetodoPagamento valor) {
            return new DadosUsuario(nome, email, senha, plano, valor, saldo, aluguelAtivo, totalPendencias);
        }

        DadosUsuario comCarteira(MetodoPagamento pagamentoAtualizado) {
            return new DadosUsuario(nome, email, senha, plano, pagamentoAtualizado,
                    pagamentoAtualizado.getSaldoOu(saldo), aluguelAtivo, totalPendencias);
        }

        DadosUsuario comAluguelAtivo(String valor) {
            return new DadosUsuario(nome, email, senha, plano, metodoPagamento, saldo, valor, totalPendencias);
        }

        DadosUsuario comPendencias(BigDecimal valor) {
            return new DadosUsuario(nome, email, senha, plano, metodoPagamento, saldo, aluguelAtivo, valor);
        }
    }

    protected UsuarioException atributoInexistente() {
        return new AtributoUsuarioNaoExisteException();
    }
}
