package br.ufal.ic.p2.bikeshare.servico.estacao;

import br.ufal.ic.p2.bikeshare.servico.estado.EstadoSistema;
import br.ufal.ic.p2.bikeshare.servico.validacao.ValidadorEntrada;
import br.ufal.ic.p2.bikeshare.dominio.estacao.Estacao;
import br.ufal.ic.p2.bikeshare.excecao.estacao.EstacaoException;
import br.ufal.ic.p2.bikeshare.excecao.estacao.CapacidadeNaoNumericaException;
import br.ufal.ic.p2.bikeshare.excecao.estacao.CapacidadeNaoPositivaException;
import br.ufal.ic.p2.bikeshare.excecao.estacao.CampoObrigatorioEstacaoException;


public final class ServicoEstacoes {
    private final EstadoSistema estado;

    public ServicoEstacoes(EstadoSistema estado) {
        this.estado = estado;
    }

    public String criar(String nome, String endereco, String capacidade) {
        validarPreenchimento(nome, "Nome nao pode ser nulo.");
        validarPreenchimento(endereco, "Endereco nao pode ser nulo.");
        validarPreenchimento(capacidade, "Capacidade nao pode ser nula.");
        int vagas = validarCapacidade(capacidade);
        Estacao estacao = new Estacao(estado.gerarIdEstacao(), nome.trim(), endereco.trim(), vagas);
        estado.atualizar(estacao);
        return estacao.getId();
    }

    public int getVagasDisponiveis(String id) {
        Estacao estacao = estado.buscarEstacao(id);
        return estado.getVagasDisponiveis(estacao);
    }

    private int validarCapacidade(String capacidade) {
        Integer valor = ValidadorEntrada.converterInteiro(capacidade)
                .orElseThrow(CapacidadeNaoNumericaException::new);
        if (valor <= 0) {
            throw new CapacidadeNaoPositivaException();
        }
        return valor;
    }

    private void validarPreenchimento(String valor, String mensagem) {
        ValidadorEntrada.exigirPreenchido(valor, () -> new CampoObrigatorioEstacaoException(mensagem));
    }
}
