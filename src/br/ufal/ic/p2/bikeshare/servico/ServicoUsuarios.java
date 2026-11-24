package br.ufal.ic.p2.bikeshare.servico;

import br.ufal.ic.p2.bikeshare.dominio.PessoaFisica;
import br.ufal.ic.p2.bikeshare.dominio.PessoaJuridica;
import br.ufal.ic.p2.bikeshare.dominio.Plano;
import br.ufal.ic.p2.bikeshare.dominio.Usuario;
import br.ufal.ic.p2.bikeshare.excecao.usuario.UsuarioException;

import static br.ufal.ic.p2.bikeshare.servico.ValidadorEntrada.emBranco;
import static br.ufal.ic.p2.bikeshare.servico.ValidadorUsuario.validarPreenchimento;


final class ServicoUsuarios {
    private static final String METODO_PAGAMENTO = "metodoPagamento";

    private final EstadoSistema estado;
    private final ServicoAlteracaoUsuario alteracao;

    ServicoUsuarios(EstadoSistema estado) {
        this.estado = estado;
        this.alteracao = new ServicoAlteracaoUsuario(estado);
    }

    String criarPessoaFisica(String nome, String email, String senha, String tipo, String cpf,
                             String nomePlano, String mensalidade) {
        validarDadosComuns(nome, email, senha);
        validarTipo(tipo, "pessoaFisica");
        validarPreenchimento(cpf, "CPF nao pode ser nulo.");
        Plano plano = FabricaPlano.criar(nomePlano, mensalidade);
        return cadastrar(new PessoaFisica(estado.gerarIdUsuario(), nome, email.trim(), senha, cpf, plano));
    }

    String criarPessoaJuridica(String nome, String email, String senha, String tipo, String cnpj,
                               String razaoSocial, String nomePlano, String mensalidade) {
        validarDadosComuns(nome, email, senha);
        validarTipo(tipo, "pessoaJuridica");
        validarPreenchimento(cnpj, "CNPJ nao pode ser nulo.");
        validarPreenchimento(razaoSocial, "Razao social nao pode ser nula.");
        Plano plano = FabricaPlano.criar(nomePlano, mensalidade);
        return cadastrar(new PessoaJuridica(estado.gerarIdUsuario(), nome, email.trim(), senha, cnpj,
                razaoSocial, plano));
    }

    String getAtributo(String id, String atributo) {
        return estado.buscarUsuario(id).getAtributo(atributo);
    }

    String getIdPorEmail(String email) {
        if (emBranco(email)) {
            throw new UsuarioException("Email nao pode ser nulo.");
        }
        Usuario usuario = estado.buscarUsuarioPorEmail(email);
        if (usuario == null) {
            throw new UsuarioException("Usuario nao existe.");
        }
        return usuario.getId();
    }

    void remover(String id) {
        if (estado.buscarUsuario(id).possuiAluguelAtivo()) {
            throw new UsuarioException("Nao e possivel remover usuario com aluguel em andamento.");
        }
        estado.removerUsuario(id);
    }

    
    void alterar(String id, String atributo, String valor) {
        if (METODO_PAGAMENTO.equals(atributo)) {
            aplicarAlteracao(id, atributo, null, valor, null, null, null);
            return;
        }
        aplicarAlteracao(id, atributo, valor, null, null, null, null);
    }

    
    void alterarComChave(String id, String atributo, String tipoPagamento, String chave) {
        if (!METODO_PAGAMENTO.equals(atributo)) {
            estado.buscarUsuario(id);
            throw new UsuarioException("Atributo nao pode ser alterado.");
        }
        aplicarAlteracao(id, atributo, null, tipoPagamento, null, null, chave);
    }

    
    void alterarComCartao(String id, String atributo, String tipoPagamento, String numero, String validade) {
        aplicarAlteracao(id, atributo, null, tipoPagamento, numero, validade, null);
    }

    private void aplicarAlteracao(String id, String atributo, String valor, String tipoPagamento,
                                  String numero, String validade, String chave) {
        Usuario usuario = estado.buscarUsuario(id);
        estado.atualizar(alteracao.alterar(usuario, atributo, valor, tipoPagamento, numero, validade, chave));
    }

    private String cadastrar(Usuario usuario) {
        if (estado.buscarUsuarioPorEmail(usuario.getEmail()) != null) {
            throw new UsuarioException("Email ja cadastrado.");
        }
        estado.atualizar(usuario);
        return usuario.getId();
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
}
