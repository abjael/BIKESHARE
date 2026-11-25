package br.ufal.ic.p2.bikeshare.servico.bicicleta;

import br.ufal.ic.p2.bikeshare.servico.estado.EstadoSistema;
import br.ufal.ic.p2.bikeshare.servico.validacao.ValidadorEntrada;
import br.ufal.ic.p2.bikeshare.dominio.bicicleta.Bicicleta;
import br.ufal.ic.p2.bikeshare.dominio.bicicleta.BicicletaConvencional;
import br.ufal.ic.p2.bikeshare.dominio.bicicleta.BicicletaEletrica;
import br.ufal.ic.p2.bikeshare.dominio.bicicleta.EstadoBicicleta;
import br.ufal.ic.p2.bikeshare.dominio.estacao.Estacao;
import br.ufal.ic.p2.bikeshare.excecao.bicicleta.BicicletaException;
import br.ufal.ic.p2.bikeshare.excecao.bicicleta.IdentificacaoBicicletaNulaException;
import br.ufal.ic.p2.bikeshare.excecao.bicicleta.BicicletaJaExisteException;
import br.ufal.ic.p2.bikeshare.excecao.bicicleta.BicicletaNaoEstaNaEstacaoException;
import br.ufal.ic.p2.bikeshare.excecao.bicicleta.BicicletaSemBateriaException;
import br.ufal.ic.p2.bikeshare.excecao.bicicleta.TipoBicicletaInvalidoException;
import br.ufal.ic.p2.bikeshare.excecao.bicicleta.NivelBateriaNuloException;
import br.ufal.ic.p2.bikeshare.excecao.bicicleta.NivelBateriaNaoNumericoException;
import br.ufal.ic.p2.bikeshare.excecao.bicicleta.NivelBateriaForaDaFaixaException;
import br.ufal.ic.p2.bikeshare.excecao.bicicleta.BicicletaNaoDisponivelException;
import br.ufal.ic.p2.bikeshare.excecao.estacao.EstacaoSemVagaException;

import static br.ufal.ic.p2.bikeshare.servico.validacao.ValidadorEntrada.emBranco;


public final class ServicoBicicletas {
    private static final int BATERIA_MINIMA = 0;
    private static final int BATERIA_MAXIMA = 100;

    private final EstadoSistema estado;

    public ServicoBicicletas(EstadoSistema estado) {
        this.estado = estado;
    }

    public int getBicicletasDisponiveis(String idEstacao) {
        estado.buscarEstacao(idEstacao);
        int disponiveis = 0;
        for (Bicicleta bicicleta : estado.getBicicletas()) {
            if (bicicleta.estaNaEstacao(idEstacao) && bicicleta.estaDisponivelParaAluguel()) {
                disponiveis++;
            }
        }
        return disponiveis;
    }

    public void adicionarBicicleta(String idEstacao, String idBicicleta, String tipo, String nivelBateria) {
        idEstacao = idEstacao == null ? null : idEstacao.trim();
        idBicicleta = idBicicleta == null ? null : idBicicleta.trim();
        Estacao estacao = estado.buscarEstacao(idEstacao);
        if (emBranco(idBicicleta)) {
            throw new IdentificacaoBicicletaNulaException();
        }
        if (estado.existeBicicleta(idBicicleta)) {
            throw new BicicletaJaExisteException();
        }
        Bicicleta bicicleta = criarBicicleta(idEstacao, idBicicleta, tipo, nivelBateria);
        if (!estado.estacaoTemVaga(estacao)) {
            throw new EstacaoSemVagaException();
        }
        estado.atualizar(bicicleta);
    }

    public void removerBicicleta(String idEstacao, String idBicicleta) {
        idEstacao = idEstacao == null ? null : idEstacao.trim();
        idBicicleta = idBicicleta == null ? null : idBicicleta.trim();
        Estacao estacao = estado.buscarEstacao(idEstacao);
        Bicicleta bicicleta = estado.buscarBicicleta(idBicicleta);
        if (!bicicleta.estaNaEstacao(idEstacao)) {
            throw new BicicletaNaoEstaNaEstacaoException();
        }
        EstadoBicicleta estadoBicicleta = EstadoBicicleta.fromRotulo(bicicleta.getEstado());
        estadoBicicleta.exigirDisponivel();
        estado.removerBicicleta(idBicicleta);
    }

    public String getAtributoBicicleta(String id, String atributo) {
        return estado.buscarBicicleta(id).getAtributo(atributo);
    }

    public String getEstadoBicicleta(String id) {
        return estado.buscarBicicleta(id).getEstado();
    }

    private Bicicleta criarBicicleta(String idEstacao, String id, String tipo, String nivelBateria) {
        if ("convencional".equals(tipo)) {
            if (nivelBateria != null) {
                throw new BicicletaSemBateriaException();
            }
            return new BicicletaConvencional(id, idEstacao);
        }
        if (!"eletrica".equals(tipo)) {
            throw new TipoBicicletaInvalidoException();
        }
        return new BicicletaEletrica(id, idEstacao, validarNivelBateria(nivelBateria));
    }

    private int validarNivelBateria(String nivelBateria) {
        if (emBranco(nivelBateria)) {
            throw new NivelBateriaNuloException();
        }
        Integer nivel = ValidadorEntrada.converterInteiro(nivelBateria)
                .orElseThrow(NivelBateriaNaoNumericoException::new);
        if (nivel < BATERIA_MINIMA || nivel > BATERIA_MAXIMA) {
            throw new NivelBateriaForaDaFaixaException();
        }
        return nivel;
    }
}
