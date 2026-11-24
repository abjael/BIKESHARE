package br.ufal.ic.p2.bikeshare.dominio;

import br.ufal.ic.p2.bikeshare.excecao.aluguel.AluguelException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;


public class Aluguel {
    public static final String EM_ANDAMENTO = "emAndamento";
    public static final String FINALIZADO = "finalizado";
    public static final String CANCELADO = "cancelado";
    public static final String EXPIRADO = "expirado";

    private final String id;
    private final String usuario;
    private final String bicicleta;
    private final String estacaoOrigem;
    private final LocalDateTime inicio;
    private final String estado;
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
        this.id = id;
        this.usuario = usuario;
        this.bicicleta = bicicleta;
        this.estacaoOrigem = estacaoOrigem;
        this.inicio = inicio;
        this.estado = estado;
        this.estacaoDestino = estacaoDestino;
        this.fim = fim;
        this.valorCobrado = valorCobrado;
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
        return estado;
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
        return EM_ANDAMENTO.equals(estado);
    }

    public long minutosAte(LocalDateTime dataFim) {
        return ChronoUnit.MINUTES.between(inicio, dataFim);
    }

    
    public boolean devolvidoNaCarencia(String idEstacao, long minutos) {
        return idEstacao.equals(estacaoOrigem) && minutos <= ConfiguracaoSistema.getMinutosCarencia();
    }

    public Aluguel encerrar(String novoEstado, String destino, LocalDateTime dataFim, BigDecimal valor) {
        return new Aluguel(id, usuario, bicicleta, estacaoOrigem, inicio, novoEstado, destino, dataFim, valor);
    }

    public String getAtributo(String atributo) {
        String atributoBasico = getAtributoBasico(atributo);
        return atributoBasico == null ? getAtributoFinal(atributo) : atributoBasico;
    }

    private String getAtributoBasico(String atributo) {
        if ("estado".equals(atributo)) {
            return estado;
        }
        if ("usuario".equals(atributo)) {
            return usuario;
        }
        if ("bicicleta".equals(atributo)) {
            return bicicleta;
        }
        if ("estacaoOrigem".equals(atributo)) {
            return estacaoOrigem;
        }
        if ("inicio".equals(atributo)) {
            return getInicioFormatado();
        }
        return null;
    }

    private String getAtributoFinal(String atributo) {
        if (!"fim".equals(atributo) && !"estacaoDestino".equals(atributo) && !"valorCobrado".equals(atributo)) {
            throw new AluguelException("Atributo nao existe.");
        }
        if (estaEmAndamento()) {
            throw new AluguelException("Aluguel ainda esta em andamento.");
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
