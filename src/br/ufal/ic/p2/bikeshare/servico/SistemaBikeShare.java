package br.ufal.ic.p2.bikeshare.servico;

import br.ufal.ic.p2.bikeshare.dominio.PessoaFisica;
import br.ufal.ic.p2.bikeshare.dominio.PessoaJuridica;
import br.ufal.ic.p2.bikeshare.dominio.Plano;
import br.ufal.ic.p2.bikeshare.dominio.PlanoAssinante;
import br.ufal.ic.p2.bikeshare.dominio.PlanoAvulso;
import br.ufal.ic.p2.bikeshare.dominio.Usuario;
import br.ufal.ic.p2.bikeshare.excecao.usuario.UsuarioException;
import br.ufal.ic.p2.bikeshare.persistencia.EstadoPersistido;
import br.ufal.ic.p2.bikeshare.persistencia.RepositorioSistema;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.function.Supplier;
import java.util.regex.Pattern;

public class SistemaBikeShare {
    private static final Pattern EMAIL_VALIDO = Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");
    private static final Pattern VALOR_NUMERICO = Pattern.compile("^-?\\d+(?:[.,]\\d+)?$");

    private final RepositorioSistema repositorio;
    private final Map<String, Usuario> usuarios;
    private int proximoIdUsuario;

    public SistemaBikeShare() {
        repositorio = new RepositorioSistema();
        EstadoPersistido estado = repositorio.carregar();
        usuarios = estado.getUsuarios();
        proximoIdUsuario = estado.getProximoIdUsuario();
    }

    public String criarPessoaFisica(String nome, String email, String senha, String tipo, String cpf,
                                    String nomePlano, String mensalidade) {
        return executarComando(() -> criarPessoaFisicaValidada(nome, email, senha, tipo, cpf,
                nomePlano, mensalidade));
    }

    private String criarPessoaFisicaValidada(String nome, String email, String senha, String tipo, String cpf,
                                             String nomePlano, String mensalidade) {
        validarDadosComuns(nome, email, senha);
        validarTipo(tipo, "pessoaFisica");
        validarPreenchimento(cpf, "CPF nao pode ser nulo.");
        return salvarNovoUsuario(new PessoaFisica(idDisponivel(), nome, email.trim(), senha, cpf,
                criarPlano(nomePlano, mensalidade)));
    }

    public String criarPessoaJuridica(String nome, String email, String senha, String tipo, String cnpj,
                                      String razaoSocial, String nomePlano, String mensalidade) {
        return executarComando(() -> criarPessoaJuridicaValidada(nome, email, senha, tipo, cnpj,
                razaoSocial, nomePlano, mensalidade));
    }

    private String criarPessoaJuridicaValidada(String nome, String email, String senha, String tipo, String cnpj,
                                               String razaoSocial, String nomePlano, String mensalidade) {
        validarDadosComuns(nome, email, senha);
        validarTipo(tipo, "pessoaJuridica");
        validarPreenchimento(cnpj, "CNPJ nao pode ser nulo.");
        validarPreenchimento(razaoSocial, "Razao social nao pode ser nula.");
        return salvarNovoUsuario(new PessoaJuridica(idDisponivel(), nome, email.trim(), senha, cnpj, razaoSocial,
                criarPlano(nomePlano, mensalidade)));
    }

    public String getAtributoUsuario(String id, String atributo) {
        if (emBranco(id)) {
            throw new UsuarioException("Identificacao do usuario nao pode ser nula.");
        }
        Usuario usuario = usuarios.get(id);
        if (usuario == null) {
            throw new UsuarioException("Usuario nao existe.");
        }
        return usuario.getAtributo(atributo);
    }

    public void zerar() {
        usuarios.clear();
        proximoIdUsuario = 1;
    }

    public void salvar() {
        repositorio.salvar(new EstadoPersistido(usuarios, proximoIdUsuario));
    }

    private void validarDadosComuns(String nome, String email, String senha) {
        validarPreenchimento(nome, "Nome nao pode ser nulo.");
        validarPreenchimento(email, "Email nao pode ser nulo.");
        if (!EMAIL_VALIDO.matcher(email.trim()).matches()) {
            throw new UsuarioException("Email invalido.");
        }
        validarPreenchimento(senha, "Senha nao pode ser nula.");
    }

    private void validarTipo(String tipo, String tipoEsperado) {
        if (!tipoEsperado.equals(tipo)) {
            throw new UsuarioException("Tipo invalido.");
        }
    }

    private Plano criarPlano(String nomePlano, String mensalidade) {
        if ("avulso".equals(nomePlano)) {
            if (!emBranco(mensalidade)) {
                throw new UsuarioException("Plano nao possui mensalidade.");
            }
            return new PlanoAvulso();
        }
        if (!"assinante".equals(nomePlano)) {
            throw new UsuarioException("Plano invalido.");
        }
        return new PlanoAssinante(validarMensalidade(mensalidade));
    }

    private BigDecimal validarMensalidade(String valor) {
        if (emBranco(valor)) {
            throw new UsuarioException("Mensalidade nao pode ser nula.");
        }
        String valorNormalizado = valor.trim();
        if (!VALOR_NUMERICO.matcher(valorNormalizado).matches()) {
            throw new UsuarioException("Mensalidade deve ser numerica.");
        }
        BigDecimal mensalidade = new BigDecimal(valorNormalizado.replace(',', '.'));
        if (mensalidade.signum() < 0) {
            throw new UsuarioException("Mensalidade deve ser nao-negativa.");
        }
        return mensalidade;
    }

    private String salvarNovoUsuario(Usuario usuario) {
        String chaveEmail = normalizarEmail(usuario.getEmail());
        for (Usuario existente : usuarios.values()) {
            if (normalizarEmail(existente.getEmail()).equals(chaveEmail)) {
                throw new UsuarioException("Email ja cadastrado.");
            }
        }
        usuarios.put(usuario.getId(), usuario);
        proximoIdUsuario++;
        return usuario.getId();
    }

    private String idDisponivel() {
        return "U" + proximoIdUsuario;
    }

    private String normalizarEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }

    private void validarPreenchimento(String valor, String mensagem) {
        if (emBranco(valor)) {
            throw new UsuarioException(mensagem);
        }
    }

    private boolean emBranco(String valor) {
        return valor == null || valor.trim().isEmpty();
    }

    private <T> T executarComando(Supplier<T> comando) {
        Map<String, Usuario> usuariosAnteriores = new LinkedHashMap<>(usuarios);
        int proximoIdAnterior = proximoIdUsuario;
        try {
            return comando.get();
        } catch (RuntimeException excecao) {
            usuarios.clear();
            usuarios.putAll(usuariosAnteriores);
            proximoIdUsuario = proximoIdAnterior;
            throw excecao;
        }
    }
}
