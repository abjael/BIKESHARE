package br.ufal.ic.p2.bikeshare.servico;

import br.ufal.ic.p2.bikeshare.persistencia.EstadoPersistido;
import br.ufal.ic.p2.bikeshare.persistencia.RepositorioSistema;
import br.ufal.ic.p2.bikeshare.servico.aluguel.ServicoAluguel;
import br.ufal.ic.p2.bikeshare.servico.bicicleta.ServicoBicicletas;
import br.ufal.ic.p2.bikeshare.servico.estacao.ServicoEstacoes;
import br.ufal.ic.p2.bikeshare.servico.estado.EstadoSistema;
import br.ufal.ic.p2.bikeshare.servico.usuario.ServicoUsuarios;

import java.util.function.Supplier;

/** Guarda o estado do sistema, a persistencia e desfaz o comando que falhar no meio. */
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

    public ServicoUsuarios usuarios() {
        return usuarios;
    }

    public ServicoEstacoes estacoes() {
        return estacoes;
    }

    public ServicoBicicletas bicicletas() {
        return bicicletas;
    }

    public ServicoAluguel alugueis() {
        return alugueis;
    }

    public void zerar() {
        estado.restaurar(EstadoPersistido.vazio());
    }

    public void salvar() {
        repositorio.salvar(estado.capturar());
    }

    public <T> T executar(Supplier<T> operacao) {
        EstadoPersistido anterior = estado.capturar();
        try {
            return operacao.get();
        } catch (RuntimeException excecao) {
            estado.restaurar(anterior);
            throw excecao;
        }
    }

    public void executar(Runnable acao) {
        executar(() -> {
            acao.run();
            return null;
        });
    }
}
