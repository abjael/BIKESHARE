package br.ufal.ic.p2.bikeshare.servico;

import br.ufal.ic.p2.bikeshare.dominio.Estacao;
import br.ufal.ic.p2.bikeshare.excecao.estacao.EstacaoException;


final class ServicoEstacoes {
    private final EstadoSistema estado;

    ServicoEstacoes(EstadoSistema estado) {
        this.estado = estado;
    }

    String criar(String nome, String endereco, String capacidade) {
        validarPreenchimento(nome, "Nome nao pode ser nulo.");
        validarPreenchimento(endereco, "Endereco nao pode ser nulo.");
        validarPreenchimento(capacidade, "Capacidade nao pode ser nula.");
        int vagas = validarCapacidade(capacidade);
        Estacao estacao = new Estacao(estado.gerarIdEstacao(), nome.trim(), endereco.trim(), vagas);
        estado.atualizar(estacao);
        return estacao.getId();
    }

    int getVagasDisponiveis(String id) {
        return estado.buscarEstacao(id).getVagasDisponiveis();
    }

    private int validarCapacidade(String capacidade) {
        Integer valor = ValidadorEntrada.converterInteiro(capacidade);
        if (valor == null) {
            throw new EstacaoException("Capacidade deve ser numerica.");
        }
        if (valor <= 0) {
            throw new EstacaoException("Capacidade deve ser positiva.");
        }
        return valor;
    }

    private void validarPreenchimento(String valor, String mensagem) {
        if (ValidadorEntrada.emBranco(valor)) {
            throw new EstacaoException(mensagem);
        }
    }
}
