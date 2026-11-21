package br.ufal.ic.p2.bikeshare.servico;

import br.ufal.ic.p2.bikeshare.dominio.PessoaFisica;
import br.ufal.ic.p2.bikeshare.dominio.PessoaJuridica;
import br.ufal.ic.p2.bikeshare.dominio.Estacao;
import br.ufal.ic.p2.bikeshare.dominio.Bicicleta;
import br.ufal.ic.p2.bikeshare.dominio.Aluguel;
import br.ufal.ic.p2.bikeshare.dominio.Plano;
import br.ufal.ic.p2.bikeshare.dominio.PlanoAssinante;
import br.ufal.ic.p2.bikeshare.dominio.PlanoAvulso;
import br.ufal.ic.p2.bikeshare.dominio.Usuario;
import br.ufal.ic.p2.bikeshare.excecao.usuario.UsuarioException;
import br.ufal.ic.p2.bikeshare.excecao.estacao.EstacaoException;
import br.ufal.ic.p2.bikeshare.persistencia.EstadoPersistido;
import br.ufal.ic.p2.bikeshare.persistencia.RepositorioSistema;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;
import java.util.regex.Pattern;

import static br.ufal.ic.p2.bikeshare.servico.ValidadorUsuario.emBranco;
import static br.ufal.ic.p2.bikeshare.servico.ValidadorUsuario.normalizarEmail;
import static br.ufal.ic.p2.bikeshare.servico.ValidadorUsuario.validarPreenchimento;

public class SistemaBikeShare {
    private final RepositorioSistema repositorio;
    private final ServicoAlteracaoUsuario servicoAlteracaoUsuario;
    private final ServicoBicicletas servicoBicicletas;
    private final ServicoAluguel servicoAluguel;
    private final Map<String, Usuario> usuarios;
    private final Map<String, Estacao> estacoes;
    private final Map<String, Bicicleta> bicicletas;
    private final Map<String, Aluguel> alugueis;
    private int proximoIdUsuario;
    private int proximoIdEstacao;
    private int proximoIdAluguel;

    public SistemaBikeShare() {
        repositorio = new RepositorioSistema();
        EstadoPersistido estado = repositorio.carregar();
        usuarios = estado.getUsuarios();
        proximoIdUsuario = estado.getProximoIdUsuario();
        estacoes = estado.getEstacoes();
        proximoIdEstacao = estado.getProximoIdEstacao();
        bicicletas = estado.getBicicletas();
        alugueis = estado.getAlugueis();
        proximoIdAluguel = estado.getProximoIdAluguel();
        servicoAlteracaoUsuario = new ServicoAlteracaoUsuario(usuarios);
        servicoBicicletas = new ServicoBicicletas(estacoes, bicicletas);
        servicoAluguel = new ServicoAluguel(usuarios, estacoes, bicicletas, alugueis);
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

    public String getUsuarioPorEmail(String email) {
        if (emBranco(email)) {
            throw new UsuarioException("Email nao pode ser nulo.");
        }
        String chaveEmail = normalizarEmail(email);
        for (Usuario usuario : usuarios.values()) {
            if (normalizarEmail(usuario.getEmail()).equals(chaveEmail)) {
                return usuario.getId();
            }
        }
        throw new UsuarioException("Usuario nao existe.");
    }

    public void removerUsuario(String id) {
        executarComando(() -> {
            Usuario usuario = buscarUsuario(id);
            if (!"nenhum".equals(usuario.getAtributo("aluguelAtivo"))) {
                throw new UsuarioException("Nao e possivel remover usuario com aluguel em andamento.");
            }
            usuarios.remove(id);
            return null;
        });
    }

    public void alterarUsuario(String id, String atributo, String valor) {
        if ("metodoPagamento".equals(atributo)) {
            alterarUsuario(id, atributo, null, valor, null, null, null);
            return;
        }
        alterarUsuario(id, atributo, valor, null, null, null, null);
    }

    public void alterarUsuario(String id, String atributo, String valor1, String chave) {
        if (!"metodoPagamento".equals(atributo)) {
            executarComando(() -> {
                buscarUsuario(id);
                throw new UsuarioException("Atributo nao pode ser alterado.");
            });
            return;
        }
        alterarUsuario(id, atributo, null, valor1, null, null, chave);
    }

    public void alterarUsuario(String id, String atributo, String valor1, String numero, String validade) {
        alterarUsuario(id, atributo, null, valor1, numero, validade, null);
    }

    private void alterarUsuario(String id, String atributo, String valor, String valor1,
                                String numero, String validade, String chave) {
        executarComando(() -> {
            Usuario usuario = buscarUsuario(id);
            Usuario atualizado = servicoAlteracaoUsuario.alterar(usuario, atributo, valor, valor1,
                    numero, validade, chave);
            usuarios.put(id, atualizado);
            return null;
        });
    }

    private Usuario buscarUsuario(String id) {
        if (emBranco(id)) {
            throw new UsuarioException("Identificacao do usuario nao pode ser nula.");
        }
        Usuario usuario = usuarios.get(id);
        if (usuario == null) {
            throw new UsuarioException("Usuario nao existe.");
        }
        return usuario;
    }

    public void zerar() {
        usuarios.clear();
        proximoIdUsuario = 1;
        estacoes.clear();
        proximoIdEstacao = 1;
        bicicletas.clear();
        alugueis.clear();
        proximoIdAluguel = 1;
    }

    public void salvar() {
        repositorio.salvar(new EstadoPersistido(usuarios, proximoIdUsuario, estacoes, proximoIdEstacao,
                bicicletas, alugueis, proximoIdAluguel));
    }

    private void validarDadosComuns(String nome, String email, String senha) {
        validarPreenchimento(nome, "Nome nao pode ser nulo.");
        ValidadorUsuario.validarEmail(email);
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
        if (!ValidadorUsuario.VALOR_NUMERICO.matcher(valorNormalizado).matches()) {
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

    public String criarEstacao(String nome, String endereco, String capacidade) {
        return executarComando(() -> criarEstacaoValidada(nome, endereco, capacidade));
    }

    private String criarEstacaoValidada(String nome, String endereco, String capacidade) {
        validarPreenchimentoEstacao(nome, "Nome nao pode ser nulo.");
        validarPreenchimentoEstacao(endereco, "Endereco nao pode ser nulo.");
        validarPreenchimentoEstacao(capacidade, "Capacidade nao pode ser nula.");

        String capacidadeNormalizada = capacidade.trim();
        if (!Pattern.compile("^-?\\d+$").matcher(capacidadeNormalizada).matches()) {
            throw new EstacaoException("Capacidade deve ser numerica.");
        }
        int valor;
        try {
            valor = Integer.parseInt(capacidadeNormalizada);
        } catch (NumberFormatException excecao) {
            throw new EstacaoException("Capacidade deve ser numerica.");
        }
        if (valor <= 0) {
            throw new EstacaoException("Capacidade deve ser positiva.");
        }

        Estacao estacao = new Estacao(idEstacaoDisponivel(), nome.trim(), endereco.trim(), valor);
        estacoes.put(estacao.getId(), estacao);
        proximoIdEstacao++;
        return estacao.getId();
    }

    public int getVagasDisponiveis(String id) {
        return buscarEstacao(id).getVagasDisponiveis();
    }

    public int getBicicletasDisponiveis(String id) {
        return servicoBicicletas.getBicicletasDisponiveis(id);
    }

    public void adicionarBicicleta(String idEstacao, String idBicicleta, String tipo) {
        adicionarBicicleta(idEstacao, idBicicleta, tipo, null);
    }

    public void adicionarBicicleta(String idEstacao, String idBicicleta, String tipo, String nivelBateria) {
        executarComando(() -> {
            servicoBicicletas.adicionarBicicleta(idEstacao, idBicicleta, tipo, nivelBateria);
            return null;
        });
    }

    public void removerBicicleta(String idEstacao, String idBicicleta) {
        executarComando(() -> {
            servicoBicicletas.removerBicicleta(idEstacao, idBicicleta);
            return null;
        });
    }

    public String getAtributoBicicleta(String id, String atributo) {
        return servicoBicicletas.getAtributoBicicleta(id, atributo);
    }

    public String getEstadoBicicleta(String id) {
        return servicoBicicletas.getEstadoBicicleta(id);
    }

    public String retirarBicicleta(String usuario, String estacao, String bicicleta, String data) {
        return executarComando(() -> {
            String idAluguel = servicoAluguel.retirarBicicleta(usuario, estacao, bicicleta,
                    data, proximoIdAluguel);
            proximoIdAluguel++;
            return idAluguel;
        });
    }

    public String getAtributoAluguel(String aluguel, String atributo) {
        return servicoAluguel.getAtributo(aluguel, atributo);
    }

    private void validarPreenchimentoEstacao(String valor, String mensagem) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new EstacaoException(mensagem);
        }
    }

    private Estacao buscarEstacao(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new EstacaoException("Identificacao da estacao nao pode ser nula.");
        }
        Estacao estacao = estacoes.get(id);
        if (estacao == null) {
            throw new EstacaoException("Estacao nao existe.");
        }
        return estacao;
    }

    private String idEstacaoDisponivel() {
        return "E" + proximoIdEstacao;
    }

    private <T> T executarComando(Supplier<T> comando) {
        Map<String, Usuario> usuariosAnteriores = new LinkedHashMap<>(usuarios);
        int proximoIdAnterior = proximoIdUsuario;
        Map<String, Estacao> estacoesAnteriores = new LinkedHashMap<>(estacoes);
        int proximoIdEstacaoAnterior = proximoIdEstacao;
        Map<String, Bicicleta> bicicletasAnteriores = new LinkedHashMap<>(bicicletas);
        Map<String, Aluguel> alugueisAnteriores = new LinkedHashMap<>(alugueis);
        int proximoIdAluguelAnterior = proximoIdAluguel;
        try {
            return comando.get();
        } catch (RuntimeException excecao) {
            usuarios.clear();
            usuarios.putAll(usuariosAnteriores);
            proximoIdUsuario = proximoIdAnterior;
            estacoes.clear();
            estacoes.putAll(estacoesAnteriores);
            proximoIdEstacao = proximoIdEstacaoAnterior;
            bicicletas.clear();
            bicicletas.putAll(bicicletasAnteriores);
            alugueis.clear();
            alugueis.putAll(alugueisAnteriores);
            proximoIdAluguel = proximoIdAluguelAnterior;
            throw excecao;
        }
    }
}
