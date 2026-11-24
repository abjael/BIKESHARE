package br.ufal.ic.p2.bikeshare.servico;

import br.ufal.ic.p2.bikeshare.dominio.Bicicleta;
import br.ufal.ic.p2.bikeshare.dominio.BicicletaConvencional;
import br.ufal.ic.p2.bikeshare.dominio.BicicletaEletrica;
import br.ufal.ic.p2.bikeshare.dominio.Estacao;
import br.ufal.ic.p2.bikeshare.excecao.bicicleta.BicicletaException;
import br.ufal.ic.p2.bikeshare.excecao.estacao.EstacaoException;

import static br.ufal.ic.p2.bikeshare.servico.ValidadorEntrada.emBranco;


final class ServicoBicicletas {
    private static final int BATERIA_MINIMA = 0;
    private static final int BATERIA_MAXIMA = 100;

    private final EstadoSistema estado;

    ServicoBicicletas(EstadoSistema estado) {
        this.estado = estado;
    }

    int getBicicletasDisponiveis(String idEstacao) {
        estado.buscarEstacao(idEstacao);
        int disponiveis = 0;
        for (Bicicleta bicicleta : estado.getBicicletas()) {
            if (bicicleta.estaNaEstacao(idEstacao) && bicicleta.estaDisponivelParaAluguel()) {
                disponiveis++;
            }
        }
        return disponiveis;
    }

    void adicionarBicicleta(String idEstacao, String idBicicleta, String tipo, String nivelBateria) {
        Estacao estacao = estado.buscarEstacao(idEstacao);
        if (emBranco(idBicicleta)) {
            throw new BicicletaException("Identificacao da bicicleta nao pode ser nula.");
        }
        if (estado.existeBicicleta(idBicicleta)) {
            throw new BicicletaException("Bicicleta ja existe.");
        }
        Bicicleta bicicleta = criarBicicleta(idEstacao, idBicicleta, tipo, nivelBateria);
        if (!estacao.possuiVagaDisponivel()) {
            throw new EstacaoException("Estacao sem vagas disponiveis.");
        }
        estado.atualizar(bicicleta);
        estado.atualizar(estacao.adicionarBicicleta());
    }

    void removerBicicleta(String idEstacao, String idBicicleta) {
        Estacao estacao = estado.buscarEstacao(idEstacao);
        Bicicleta bicicleta = estado.buscarBicicleta(idBicicleta);
        if (!bicicleta.estaNaEstacao(idEstacao)) {
            throw new BicicletaException("Bicicleta nao esta nesta estacao.");
        }
        estado.removerBicicleta(idBicicleta);
        estado.atualizar(estacao.removerBicicleta());
    }

    String getAtributoBicicleta(String id, String atributo) {
        return estado.buscarBicicleta(id).getAtributo(atributo);
    }

    String getEstadoBicicleta(String id) {
        return estado.buscarBicicleta(id).getEstado();
    }

    private Bicicleta criarBicicleta(String idEstacao, String id, String tipo, String nivelBateria) {
        if ("convencional".equals(tipo)) {
            if (nivelBateria != null) {
                throw new BicicletaException("Bicicleta nao possui bateria.");
            }
            return new BicicletaConvencional(id, idEstacao);
        }
        if (!"eletrica".equals(tipo)) {
            throw new BicicletaException("Tipo invalido.");
        }
        return new BicicletaEletrica(id, idEstacao, validarNivelBateria(nivelBateria));
    }

    private int validarNivelBateria(String nivelBateria) {
        if (emBranco(nivelBateria)) {
            throw new BicicletaException("Nivel de bateria nao pode ser nulo.");
        }
        Integer nivel = ValidadorEntrada.converterInteiro(nivelBateria);
        if (nivel == null) {
            throw new BicicletaException("Nivel de bateria deve ser numerico.");
        }
        if (nivel < BATERIA_MINIMA || nivel > BATERIA_MAXIMA) {
            throw new BicicletaException("Nivel de bateria deve estar entre 0 e 100.");
        }
        return nivel;
    }
}
