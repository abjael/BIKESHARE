package br.ufal.ic.p2.bikeshare.dominio;

import br.ufal.ic.p2.bikeshare.excecao.usuario.UsuarioException;

import java.math.BigDecimal;

public abstract class Usuario {
    private final String id;
    private final String nome;
    private final String email;
    private final String senha;
    private final Plano plano;
    private final MetodoPagamento metodoPagamento;
    private final BigDecimal saldoPreCarregado;

    protected Usuario(String id, String nome, String email, String senha, Plano plano) {
        this(id, nome, email, senha, plano, null, BigDecimal.ZERO);
    }

    protected Usuario(String id, String nome, String email, String senha, Plano plano,
                      MetodoPagamento metodoPagamento, BigDecimal saldoPreCarregado) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.plano = plano;
        this.metodoPagamento = metodoPagamento;
        this.saldoPreCarregado = saldoPreCarregado;
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
        return saldoPreCarregado;
    }

    public Usuario comNome(String novoNome) {
        return criarCopia(novoNome, email, senha, plano, metodoPagamento, saldoPreCarregado);
    }

    public Usuario comEmail(String novoEmail) {
        return criarCopia(nome, novoEmail, senha, plano, metodoPagamento, saldoPreCarregado);
    }

    public Usuario comSenha(String novaSenha) {
        return criarCopia(nome, email, novaSenha, plano, metodoPagamento, saldoPreCarregado);
    }

    public Usuario comPlano(Plano novoPlano) {
        return criarCopia(nome, email, senha, novoPlano, metodoPagamento, saldoPreCarregado);
    }

    public Usuario comMetodoPagamento(MetodoPagamento novoMetodoPagamento) {
        return criarCopia(nome, email, senha, plano, novoMetodoPagamento, saldoPreCarregado);
    }

    public Usuario comSaldo(BigDecimal novoSaldo) {
        if (metodoPagamento == null) {
            throw new UsuarioException("Usuario nao usa saldo pre-carregado.");
        }
        MetodoPagamento pagamentoAtualizado = metodoPagamento.comSaldo(novoSaldo);
        return criarCopia(nome, email, senha, plano, pagamentoAtualizado, novoSaldo);
    }

    public String getAtributo(String atributo) {
        String valorComum = getAtributoComum(atributo);
        if (valorComum != null) {
            return valorComum;
        }
        String valorPagamento = getAtributoPagamento(atributo);
        if (valorPagamento != null) {
            return valorPagamento;
        }
        return getAtributoEspecifico(atributo);
    }

    private String getAtributoComum(String atributo) {
        if (atributo == null) {
            return null;
        }
        String atributoBasico = getAtributoBasico(atributo);
        return atributoBasico != null ? atributoBasico : getAtributoDoPlano(atributo);
    }

    private String getAtributoBasico(String atributo) {
        switch (atributo) {
            case "nome":
                return nome;
            case "email":
                return email;
            case "senha":
                return senha;
            case "tipo":
                return getTipo();
            case "estado":
                return "ativo";
            case "aluguelAtivo":
                return "nenhum";
            case "totalPendencias":
                return "0,00";
            default:
                return null;
        }
    }

    private String getAtributoPagamento(String atributo) {
        if ("metodoPagamento".equals(atributo)) {
            return metodoPagamento == null ? "nenhum" : metodoPagamento.getNome();
        }
        if (!ehAtributoDePagamento(atributo)) {
            return null;
        }
        if (metodoPagamento == null) {
            throw metodoNaoConfigurado(atributo);
        }
        return metodoPagamento.getAtributo(atributo);
    }

    private boolean ehAtributoDePagamento(String atributo) {
        return "numeroCartao".equals(atributo) || "validadeCartao".equals(atributo)
                || "chavePix".equals(atributo) || "saldo".equals(atributo);
    }

    private UsuarioException metodoNaoConfigurado(String atributo) {
        if ("numeroCartao".equals(atributo) || "validadeCartao".equals(atributo)) {
            return new UsuarioException("Usuario nao usa cartao.");
        }
        if ("chavePix".equals(atributo)) {
            return new UsuarioException("Usuario nao usa pix.");
        }
        return new UsuarioException("Usuario nao usa saldo pre-carregado.");
    }

    private String getAtributoDoPlano(String atributo) {
        switch (atributo) {
            case "plano":
                return plano.getNome();
            case "mensalidade":
                return plano.getMensalidadeFormatada();
            default:
                return null;
        }
    }

    protected abstract String getTipo();

    protected abstract String getAtributoEspecifico(String atributo);

    protected abstract Usuario criarCopia(String nome, String email, String senha, Plano plano,
                                          MetodoPagamento metodoPagamento, BigDecimal saldoPreCarregado);

    protected UsuarioException atributoInexistente() {
        return new UsuarioException("Atributo nao existe.");
    }
}
