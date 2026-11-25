package br.ufal.ic.p2.bikeshare.dominio.aluguel;

import br.ufal.ic.p2.bikeshare.dominio.util.ConfiguracaoSistema;
import br.ufal.ic.p2.bikeshare.dominio.util.FormatoDataHora;
import br.ufal.ic.p2.bikeshare.dominio.util.FormatoMoeda;
import br.ufal.ic.p2.bikeshare.excecao.AtributoInexistenteException;
import br.ufal.ic.p2.bikeshare.excecao.aluguel.AluguelAindaEmAndamentoException;
import br.ufal.ic.p2.bikeshare.excecao.aluguel.DadosAluguelInvalidosException;
import br.ufal.ic.p2.bikeshare.excecao.aluguel.DataDevolucaoAnteriorException;
import br.ufal.ic.p2.bikeshare.excecao.aluguel.IdentificacaoAluguelNulaException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

public class Aluguel {
    public static final String EM_ANDAMENTO = EstadoAluguel.EM_ANDAMENTO.getRotulo();
    public static final String FINALIZADO = EstadoAluguel.FINALIZADO.getRotulo();
    public static final String CANCELADO = EstadoAluguel.CANCELADO.getRotulo();
    public static final String EXPIRADO = EstadoAluguel.EXPIRADO.getRotulo();

    private final String id;
    private final String usuario;
    private final String bicicleta;
    private final String estacaoOrigem;
    private final LocalDateTime inicio;
    private final EstadoAluguel estado;
    private final String estacaoDestino;
    private final LocalDateTime fim;
    private final BigDecimal valorCobrado;

    public Aluguel(String id, String usuario, String bicicleta, String estacaoOrigem, LocalDateTime inicio) {
        this(id, usuario, bicicleta, estacaoOrigem, inicio, EM_ANDAMENTO, null, null, null);
    }

    public Aluguel(String id, String usuario, String bicicleta, String estacaoOrigem, String inicio,
                   String estado, String estacaoDestino, String fim, String valorCobrado) {
        this(id, usuario, bicicleta, estacaoOrigem, FormatoDataHora.parse(inicio), estado, estacaoDestino,
                fim == null ? null : FormatoDataHora.parse(fim),
                valorCobrado == null ? null : new BigDecimal(valorCobrado));
    }

    private Aluguel(String id, String usuario, String bicicleta, String estacaoOrigem, LocalDateTime inicio,
                    String estado, String estacaoDestino, LocalDateTime fim, BigDecimal valorCobrado) {
        if (id == null || id.trim().isEmpty() || usuario == null || usuario.trim().isEmpty()
                || bicicleta == null || bicicleta.trim().isEmpty() || estacaoOrigem == null
                || estacaoOrigem.trim().isEmpty() || inicio == null) {
            throw new IdentificacaoAluguelNulaException();
        }
        EstadoAluguel estadoValidado = EstadoAluguel.fromRotulo(estado);
        validarConsistenciaEstado(estadoValidado, estacaoDestino, fim, valorCobrado, inicio);
        this.id = id.trim();
        this.usuario = usuario.trim();
        this.bicicleta = bicicleta.trim();
        this.estacaoOrigem = estacaoOrigem.trim();
        this.inicio = inicio;
        this.estado = estadoValidado;
        this.estacaoDestino = estacaoDestino == null ? null : estacaoDestino.trim();
        this.fim = fim;
        this.valorCobrado = valorCobrado;
    }

    private void validarConsistenciaEstado(EstadoAluguel estadoValidado, String destino, LocalDateTime fim,
                                           BigDecimal valor, LocalDateTime inicio) {
        if (estadoValidado == EstadoAluguel.EM_ANDAMENTO) {
            if (destino != null || fim != null || valor != null) {
                throw new DadosAluguelInvalidosException();
            }
            return;
        }
        if (destino == null || destino.trim().isEmpty() || fim == null || valor == null
                || fim.isBefore(inicio) || valor.signum() < 0) {
            throw new DadosAluguelInvalidosException();
        }
    }

    public String getId() {
        return id;
    }

    public String getUsuario() {
        return usuario;
    }

    public String getBicicleta() {
        return bicicleta;
    }

    public String getEstacaoOrigem() {
        return estacaoOrigem;
    }

    public LocalDateTime getInicio() {
        return inicio;
    }

    public String getInicioFormatado() {
        return FormatoDataHora.formatar(inicio);
    }

    public String getEstado() {
        return estado.getRotulo();
    }

    public String getEstacaoDestino() {
        return estacaoDestino;
    }

    public String getFimFormatado() {
        return fim == null ? null : FormatoDataHora.formatar(fim);
    }

    public BigDecimal getValorCobrado() {
        return valorCobrado;
    }

    public boolean estaEmAndamento() {
        return estado == EstadoAluguel.EM_ANDAMENTO;
    }

    public long minutosAte(LocalDateTime dataFim) {
        return ChronoUnit.MINUTES.between(inicio, dataFim);
    }

    public boolean devolvidoNaCarencia(String idEstacao, long minutos) {
        return idEstacao.equals(estacaoOrigem) && minutos <= ConfiguracaoSistema.getMinutosCarencia();
    }

    public Aluguel finalizar(String destino, LocalDateTime dataFim, BigDecimal valor) {
        return encerrar(EstadoAluguel.FINALIZADO, destino, dataFim, valor);
    }

    public Aluguel cancelar(String destino, LocalDateTime dataFim) {
        return encerrar(EstadoAluguel.CANCELADO, destino, dataFim, BigDecimal.ZERO);
    }

    public Aluguel expirar(String destino, LocalDateTime dataFim) {
        return encerrar(EstadoAluguel.EXPIRADO, destino, dataFim, BigDecimal.ZERO);
    }

    private Aluguel encerrar(EstadoAluguel novoEstado, String destino, LocalDateTime dataFim, BigDecimal valor) {
        estado.exigirEmAndamento();
        if (dataFim == null || dataFim.isBefore(inicio)) {
            throw new DataDevolucaoAnteriorException();
        }
        if (valor == null || valor.signum() < 0) {
            throw new DadosAluguelInvalidosException();
        }
        return new Aluguel(id, usuario, bicicleta, estacaoOrigem, inicio, novoEstado.getRotulo(), destino,
                dataFim, valor);
    }

    public String getAtributo(String atributo) {
        return getAtributoBasico(atributo).orElseGet(() -> getAtributoFinal(atributo));
    }

    private Optional<String> getAtributoBasico(String atributo) {
        if ("estado".equals(atributo)) {
            return Optional.of(estado.getRotulo());
        }
        if ("usuario".equals(atributo)) {
            return Optional.of(usuario);
        }
        if ("bicicleta".equals(atributo)) {
            return Optional.of(bicicleta);
        }
        if ("estacaoOrigem".equals(atributo)) {
            return Optional.of(estacaoOrigem);
        }
        if ("inicio".equals(atributo)) {
            return Optional.of(getInicioFormatado());
        }
        return Optional.empty();
    }

    private String getAtributoFinal(String atributo) {
        if (!"fim".equals(atributo) && !"estacaoDestino".equals(atributo) && !"valorCobrado".equals(atributo)) {
            throw new AtributoInexistenteException();
        }
        if (estaEmAndamento()) {
            throw new AluguelAindaEmAndamentoException();
        }
        if ("fim".equals(atributo)) {
            return getFimFormatado();
        }
        if ("estacaoDestino".equals(atributo)) {
            return estacaoDestino;
        }
        return FormatoMoeda.formatar(valorCobrado);
    }
}
