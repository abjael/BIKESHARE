package br.ufal.ic.p2.bikeshare.servico;

import br.ufal.ic.p2.bikeshare.dominio.Bicicleta;
import br.ufal.ic.p2.bikeshare.dominio.BicicletaConvencional;
import br.ufal.ic.p2.bikeshare.dominio.BicicletaEletrica;
import br.ufal.ic.p2.bikeshare.dominio.Estacao;
import br.ufal.ic.p2.bikeshare.excecao.bicicleta.BicicletaException;
import br.ufal.ic.p2.bikeshare.excecao.estacao.EstacaoException;

import java.util.Map;
import java.util.regex.Pattern;

import static br.ufal.ic.p2.bikeshare.servico.ValidadorUsuario.emBranco;

final class ServicoBicicletas {
    private static final Pattern NIVEL_BATERIA_NUMERICO = Pattern.compile("^-?\\d+$");

    private final Map<String, Estacao> estacoes;
    private final Map<String, Bicicleta> bicicletas;

    ServicoBicicletas(Map<String, Estacao> estacoes, Map<String, Bicicleta> bicicletas) {
        this.estacoes = estacoes;
        this.bicicletas = bicicletas;
    }

    int getBicicletasDisponiveis(String idEstacao) {
        buscarEstacao(idEstacao);
        int disponiveis = 0;
        for (Bicicleta bicicleta : bicicletas.values()) {
            if (idEstacao.equals(bicicleta.getEstacao()) && bicicleta.estaDisponivelParaAluguel()) {
                disponiveis++;
            }
        }
        return disponiveis;
    }

    void adicionarBicicleta(String idEstacao, String idBicicleta, String tipo, String nivelBateria) {
        Estacao estacao = buscarEstacao(idEstacao);
        validarIdBicicleta(idBicicleta);
        if (bicicletas.containsKey(idBicicleta)) {
            throw new BicicletaException("Bicicleta ja existe.");
        }
        Bicicleta bicicleta = criarBicicleta(idEstacao, idBicicleta, tipo, nivelBateria);
        if (!estacao.possuiVagaDisponivel()) {
            throw new EstacaoException("Estacao sem vagas disponiveis.");
        }
        bicicletas.put(idBicicleta, bicicleta);
        estacoes.put(idEstacao, estacao.adicionarBicicleta());
    }

    void removerBicicleta(String idEstacao, String idBicicleta) {
        Estacao estacao = buscarEstacao(idEstacao);
        Bicicleta bicicleta = buscarBicicleta(idBicicleta);
        if (!idEstacao.equals(bicicleta.getEstacao())) {
            throw new BicicletaException("Bicicleta nao esta nesta estacao.");
        }
        bicicletas.remove(idBicicleta);
        estacoes.put(idEstacao, estacao.removerBicicleta());
    }

    String getAtributoBicicleta(String id, String atributo) {
        return buscarBicicleta(id).getAtributo(atributo);
    }

    String getEstadoBicicleta(String id) {
        return buscarBicicleta(id).getEstado();
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
        String nivelNormalizado = nivelBateria.trim();
        if (!NIVEL_BATERIA_NUMERICO.matcher(nivelNormalizado).matches()) {
            throw new BicicletaException("Nivel de bateria deve ser numerico.");
        }
        int nivel;
        try {
            nivel = Integer.parseInt(nivelNormalizado);
        } catch (NumberFormatException excecao) {
            throw new BicicletaException("Nivel de bateria deve ser numerico.");
        }
        if (nivel < 0 || nivel > 100) {
            throw new BicicletaException("Nivel de bateria deve estar entre 0 e 100.");
        }
        return nivel;
    }

    private void validarIdBicicleta(String id) {
        if (emBranco(id)) {
            throw new BicicletaException("Identificacao da bicicleta nao pode ser nula.");
        }
    }

    private Bicicleta buscarBicicleta(String id) {
        validarIdBicicleta(id);
        Bicicleta bicicleta = bicicletas.get(id);
        if (bicicleta == null) {
            throw new BicicletaException("Bicicleta nao existe.");
        }
        return bicicleta;
    }

    private Estacao buscarEstacao(String id) {
        if (emBranco(id)) {
            throw new EstacaoException("Identificacao da estacao nao pode ser nula.");
        }
        Estacao estacao = estacoes.get(id);
        if (estacao == null) {
            throw new EstacaoException("Estacao nao existe.");
        }
        return estacao;
    }
}
