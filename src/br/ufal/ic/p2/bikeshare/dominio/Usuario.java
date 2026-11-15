package br.ufal.ic.p2.bikeshare.dominio;

import br.ufal.ic.p2.bikeshare.excecao.usuario.UsuarioException;

import java.io.Serializable;

public abstract class Usuario implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String id;
    private final String nome;
    private final String email;
    private final String senha;
    private final Plano plano;

    protected Usuario(String id, String nome, String email, String senha, Plano plano) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.plano = plano;
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

    public String getAtributo(String atributo) {
        String valorComum = getAtributoComum(atributo);
        if (valorComum != null) {
            return valorComum;
        }
        validarAtributoPagamento(atributo);
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
            case "nome": return nome;
            case "email": return email;
            case "senha": return senha;
            case "tipo": return getTipo();
            case "estado": return "ativo";
            case "aluguelAtivo": return "nenhum";
            case "totalPendencias": return "0,00";
            case "metodoPagamento": return "nenhum";
            default: return null;
        }
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

    private void validarAtributoPagamento(String atributo) {
        if ("numeroCartao".equals(atributo) || "validadeCartao".equals(atributo)) {
            throw new UsuarioException("Usuario nao usa cartao.");
        }
        if ("chavePix".equals(atributo)) {
            throw new UsuarioException("Usuario nao usa pix.");
        }
        if ("saldo".equals(atributo)) {
            throw new UsuarioException("Usuario nao usa saldo pre-carregado.");
        }
    }

    protected abstract String getTipo();

    protected abstract String getAtributoEspecifico(String atributo);

    protected UsuarioException atributoInexistente() {
        return new UsuarioException("Atributo nao existe.");
    }
}
