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
    private final String aluguelAtivo;
    private final BigDecimal totalPendencias;

    protected Usuario(String id, String nome, String email, String senha, Plano plano) {
        this(id, nome, email, senha, plano, null, BigDecimal.ZERO, null, BigDecimal.ZERO);
    }

    protected Usuario(String id, String nome, String email, String senha, Plano plano,
                      MetodoPagamento metodoPagamento, BigDecimal saldoPreCarregado, String aluguelAtivo,
                      BigDecimal totalPendencias) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.plano = plano;
        this.metodoPagamento = metodoPagamento;
        this.saldoPreCarregado = saldoPreCarregado;
        this.aluguelAtivo = aluguelAtivo;
        this.totalPendencias = totalPendencias;
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
        return metodoPagamento == null ? saldoPreCarregado : metodoPagamento.getSaldoOu(saldoPreCarregado);
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
        return criarCopia(novoNome, email, senha, plano, metodoPagamento, saldoPreCarregado, aluguelAtivo,
                totalPendencias);
    }

    public Usuario comEmail(String novoEmail) {
        return criarCopia(nome, novoEmail, senha, plano, metodoPagamento, saldoPreCarregado, aluguelAtivo,
                totalPendencias);
    }

    public Usuario comSenha(String novaSenha) {
        return criarCopia(nome, email, novaSenha, plano, metodoPagamento, saldoPreCarregado, aluguelAtivo,
                totalPendencias);
    }

    public Usuario comPlano(Plano novoPlano) {
        return criarCopia(nome, email, senha, novoPlano, metodoPagamento, saldoPreCarregado, aluguelAtivo,
                totalPendencias);
    }

    public Usuario comMetodoPagamento(MetodoPagamento novoMetodoPagamento) {
        return criarCopia(nome, email, senha, plano, novoMetodoPagamento, saldoPreCarregado, aluguelAtivo,
                totalPendencias);
    }

    public Usuario comSaldo(BigDecimal novoSaldo) {
        if (metodoPagamento == null) {
            throw new UsuarioException("Usuario nao usa saldo pre-carregado.");
        }
        MetodoPagamento pagamentoAtualizado = metodoPagamento.comSaldo(novoSaldo);
        return criarCopia(nome, email, senha, plano, pagamentoAtualizado, novoSaldo, aluguelAtivo, totalPendencias);
    }

    public Usuario comAluguelAtivo(String novoAluguelAtivo) {
        return criarCopia(nome, email, senha, plano, metodoPagamento, saldoPreCarregado, novoAluguelAtivo,
                totalPendencias);
    }

    public Usuario cobrar(BigDecimal valor) {
        MetodoPagamento pagamentoAtualizado = metodoPagamento.cobrar(valor);
        BigDecimal saldoAtualizado = pagamentoAtualizado.getSaldoOu(saldoPreCarregado);
        return criarCopia(nome, email, senha, plano, pagamentoAtualizado, saldoAtualizado, aluguelAtivo,
                totalPendencias);
    }

    public Usuario adicionarPendencia(BigDecimal valor) {
        return criarCopia(nome, email, senha, plano, metodoPagamento, saldoPreCarregado, aluguelAtivo,
                totalPendencias.add(valor));
    }

    
    public void validarPagamentoPendente() {
        if (metodoPagamento == null) {
            throw new UsuarioException("Metodo de pagamento nao cadastrado.");
        }
        metodoPagamento.validarPagamentoPendente();
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
        String cadastral = getAtributoCadastral(atributo);
        if (cadastral != null) {
            return cadastral;
        }
        String situacao = getAtributoSituacao(atributo);
        return situacao != null ? situacao : getAtributoDoPlano(atributo);
    }

    private String getAtributoCadastral(String atributo) {
        switch (atributo) {
            case "nome":
                return nome;
            case "email":
                return email;
            case "senha":
                return senha;
            case "tipo":
                return getTipo();
            default:
                return null;
        }
    }

    private String getAtributoSituacao(String atributo) {
        switch (atributo) {
            case "estado":
                return estaInadimplente() ? "inadimplente" : "ativo";
            case "aluguelAtivo":
                return possuiAluguelAtivo() ? aluguelAtivo : "nenhum";
            case "totalPendencias":
                return FormatoMoeda.formatar(totalPendencias);
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
            throw MetodoPagamento.atributoNaoSuportado(atributo);
        }
        return metodoPagamento.getAtributo(atributo);
    }

    private boolean ehAtributoDePagamento(String atributo) {
        return "numeroCartao".equals(atributo) || "validadeCartao".equals(atributo)
                || "chavePix".equals(atributo) || "saldo".equals(atributo);
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
                                          MetodoPagamento metodoPagamento, BigDecimal saldoPreCarregado,
                                          String aluguelAtivo, BigDecimal totalPendencias);

    protected UsuarioException atributoInexistente() {
        return new UsuarioException("Atributo nao existe.");
    }
}
