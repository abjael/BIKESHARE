package br.ufal.ic.p2.bikeshare.servico.usuario;

import br.ufal.ic.p2.bikeshare.servico.plano.FabricaPlano;
import br.ufal.ic.p2.bikeshare.servico.estado.EstadoSistema;
import br.ufal.ic.p2.bikeshare.servico.validacao.ValidadorEntrada;
import br.ufal.ic.p2.bikeshare.dominio.plano.Plano;
import br.ufal.ic.p2.bikeshare.dominio.usuario.Usuario;
import br.ufal.ic.p2.bikeshare.dominio.usuario.PessoaFisica;
import br.ufal.ic.p2.bikeshare.dominio.usuario.PessoaJuridica;
import br.ufal.ic.p2.bikeshare.excecao.usuario.UsuarioException;
import br.ufal.ic.p2.bikeshare.excecao.usuario.EmailNaoPodeSerNuloException;
import br.ufal.ic.p2.bikeshare.excecao.usuario.UsuarioNaoExisteException;
import br.ufal.ic.p2.bikeshare.excecao.usuario.UsuarioComAluguelException;
import br.ufal.ic.p2.bikeshare.excecao.usuario.AtributoNaoPodeSerAlteradoException;
import br.ufal.ic.p2.bikeshare.excecao.usuario.EmailJaCadastradoException;
import br.ufal.ic.p2.bikeshare.excecao.usuario.TipoUsuarioInvalidoException;

import static br.ufal.ic.p2.bikeshare.servico.validacao.ValidadorEntrada.emBranco;
import static br.ufal.ic.p2.bikeshare.servico.usuario.ValidadorUsuario.validarPreenchimento;


public final class ServicoUsuarios {
    private static final String METODO_PAGAMENTO = "metodoPagamento";

    private final EstadoSistema estado;
    private final ServicoAlteracaoUsuario alteracao;

    public ServicoUsuarios(EstadoSistema estado) {
        this.estado = estado;
        this.alteracao = new ServicoAlteracaoUsuario(estado);
    }

    public String criarPessoaFisica(String nome, String email, String senha, String tipo, String cpf,
                             String nomePlano, String mensalidade) {
        validarDadosComuns(nome, email, senha);
        validarTipo(tipo, "pessoaFisica");
        validarPreenchimento(cpf, "CPF nao pode ser nulo.");
        Plano plano = FabricaPlano.criar(nomePlano, mensalidade);
        return cadastrar(new PessoaFisica(estado.gerarIdUsuario(), nome, email.trim(), senha, cpf, plano));
    }

    public String criarPessoaJuridica(String nome, String email, String senha, String tipo, String cnpj,
                               String razaoSocial, String nomePlano, String mensalidade) {
        validarDadosComuns(nome, email, senha);
        validarTipo(tipo, "pessoaJuridica");
        validarPreenchimento(cnpj, "CNPJ nao pode ser nulo.");
        validarPreenchimento(razaoSocial, "Razao social nao pode ser nula.");
        Plano plano = FabricaPlano.criar(nomePlano, mensalidade);
        return cadastrar(new PessoaJuridica(estado.gerarIdUsuario(), nome, email.trim(), senha, cnpj,
                razaoSocial, plano));
    }

    public String getAtributo(String id, String atributo) {
        return estado.buscarUsuario(id).getAtributo(atributo);
    }

    public String getIdPorEmail(String email) {
        if (emBranco(email)) {
            throw new EmailNaoPodeSerNuloException();
        }
        Usuario usuario = estado.buscarUsuarioPorEmail(email).orElseThrow(UsuarioNaoExisteException::new);
        return usuario.getId();
    }

    public void remover(String id) {
        if (estado.buscarUsuario(id).possuiAluguelAtivo()) {
            throw new UsuarioComAluguelException();
        }
        estado.removerUsuario(id);
    }

    
    public void alterar(String id, String atributo, String valor) {
        Usuario usuario = estado.buscarUsuario(id);
        estado.atualizar(alteracao.alterarAtributo(usuario, atributo, valor));
    }

    
    public void alterarComChave(String id, String atributo, String tipoPagamento, String chave) {
        if (!METODO_PAGAMENTO.equals(atributo)) {
            estado.buscarUsuario(id);
            throw new AtributoNaoPodeSerAlteradoException();
        }
        Usuario usuario = estado.buscarUsuario(id);
        estado.atualizar(alteracao.alterarComChave(usuario, tipoPagamento, chave));
    }

    
    public void alterarComCartao(String id, String atributo, String tipoPagamento, String numero, String validade) {
        if (!METODO_PAGAMENTO.equals(atributo)) {
            estado.buscarUsuario(id);
            throw new AtributoNaoPodeSerAlteradoException();
        }
        Usuario usuario = estado.buscarUsuario(id);
        estado.atualizar(alteracao.alterarComCartao(usuario, tipoPagamento, numero, validade));
    }

    private String cadastrar(Usuario usuario) {
        if (estado.buscarUsuarioPorEmail(usuario.getEmail()).isPresent()) {
            throw new EmailJaCadastradoException();
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
            throw new TipoUsuarioInvalidoException();
        }
    }
}
