package br.ufal.ic.p2.bikeshare.servico;

import br.ufal.ic.p2.bikeshare.persistencia.EstadoPersistido;
import br.ufal.ic.p2.bikeshare.persistencia.RepositorioSistema;

import java.util.function.Supplier;


public class SistemaBikeShare {
    private final RepositorioSistema repositorio = new RepositorioSistema();
    private final EstadoSistema estado;
    private final ServicoUsuarios usuarios;
    private final ServicoEstacoes estacoes;
    private final ServicoBicicletas bicicletas;
    private final ServicoAluguel alugueis;

    
    public SistemaBikeShare() {
        estado = new EstadoSistema(repositorio.carregar());
        usuarios = new ServicoUsuarios(estado);
        estacoes = new ServicoEstacoes(estado);
        bicicletas = new ServicoBicicletas(estado);
        alugueis = new ServicoAluguel(estado);
    }

    public String criarPessoaFisica(String nome, String email, String senha, String tipo, String cpf,
                                    String plano, String mensalidade) {
        return executar(() -> usuarios.criarPessoaFisica(nome, email, senha, tipo, cpf, plano, mensalidade));
    }

    public String criarPessoaJuridica(String nome, String email, String senha, String tipo, String cnpj,
                                      String razaoSocial, String plano, String mensalidade) {
        return executar(() -> usuarios.criarPessoaJuridica(nome, email, senha, tipo, cnpj, razaoSocial,
                plano, mensalidade));
    }

    public String getAtributoUsuario(String id, String atributo) {
        return usuarios.getAtributo(id, atributo);
    }

    public String getUsuarioPorEmail(String email) {
        return usuarios.getIdPorEmail(email);
    }

    public void removerUsuario(String id) {
        executarAcao(() -> usuarios.remover(id));
    }

    public void alterarUsuario(String id, String atributo, String valor) {
        executarAcao(() -> usuarios.alterar(id, atributo, valor));
    }

    public void alterarUsuario(String id, String atributo, String tipoPagamento, String chave) {
        executarAcao(() -> usuarios.alterarComChave(id, atributo, tipoPagamento, chave));
    }

    public void alterarUsuario(String id, String atributo, String tipoPagamento, String numero, String validade) {
        executarAcao(() -> usuarios.alterarComCartao(id, atributo, tipoPagamento, numero, validade));
    }

    public String criarEstacao(String nome, String endereco, String capacidade) {
        return executar(() -> estacoes.criar(nome, endereco, capacidade));
    }

    public int getVagasDisponiveis(String id) {
        return estacoes.getVagasDisponiveis(id);
    }

    public int getBicicletasDisponiveis(String id) {
        return bicicletas.getBicicletasDisponiveis(id);
    }

    public void adicionarBicicleta(String idEstacao, String idBicicleta, String tipo, String nivelBateria) {
        executarAcao(() -> bicicletas.adicionarBicicleta(idEstacao, idBicicleta, tipo, nivelBateria));
    }

    public void removerBicicleta(String idEstacao, String idBicicleta) {
        executarAcao(() -> bicicletas.removerBicicleta(idEstacao, idBicicleta));
    }

    public String getAtributoBicicleta(String id, String atributo) {
        return bicicletas.getAtributoBicicleta(id, atributo);
    }

    public String getEstadoBicicleta(String id) {
        return bicicletas.getEstadoBicicleta(id);
    }

    public String retirarBicicleta(String usuario, String estacao, String bicicleta, String data) {
        return executar(() -> alugueis.retirarBicicleta(usuario, estacao, bicicleta, data));
    }

    public String getAtributoAluguel(String aluguel, String atributo) {
        return alugueis.getAtributo(aluguel, atributo);
    }

    public void devolverBicicleta(String aluguel, String estacao, String data) {
        executarAcao(() -> alugueis.devolverBicicleta(aluguel, estacao, data));
    }

    
    public void zerar() {
        estado.restaurar(EstadoPersistido.vazio());
    }

    
    public void salvar() {
        repositorio.salvar(estado.capturar());
    }

    private void executarAcao(Runnable acao) {
        executar(() -> {
            acao.run();
            return null;
        });
    }

    private <T> T executar(Supplier<T> operacao) {
        EstadoPersistido anterior = estado.capturar();
        try {
            return operacao.get();
        } catch (RuntimeException excecao) {
            estado.restaurar(anterior);
            throw excecao;
        }
    }
}
