package br.ufal.ic.p2.bikeshare.persistencia;

import br.ufal.ic.p2.bikeshare.excecao.BikeShareException;
import br.ufal.ic.p2.bikeshare.dominio.Cartao;
import br.ufal.ic.p2.bikeshare.dominio.Aluguel;
import br.ufal.ic.p2.bikeshare.dominio.BicicletaConvencional;
import br.ufal.ic.p2.bikeshare.dominio.BicicletaEletrica;
import br.ufal.ic.p2.bikeshare.dominio.Estacao;
import br.ufal.ic.p2.bikeshare.dominio.PlanoAssinante;
import br.ufal.ic.p2.bikeshare.dominio.PlanoAvulso;
import br.ufal.ic.p2.bikeshare.dominio.PessoaFisica;
import br.ufal.ic.p2.bikeshare.dominio.PessoaJuridica;
import br.ufal.ic.p2.bikeshare.dominio.Pix;
import br.ufal.ic.p2.bikeshare.dominio.SaldoPreCarregado;

import java.beans.Expression;
import java.beans.PersistenceDelegate;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;


public class RepositorioSistema {
    private static final File ARQUIVO = new File("bikeshare_data.xml");
    private static final File ARQUIVO_TEMPORARIO = new File("bikeshare_data.xml.tmp");

    public EstadoPersistido carregar() {
        if (!ARQUIVO.exists()) {
            return EstadoPersistido.vazio();
        }
        AtomicReference<Exception> erroLeitura = new AtomicReference<>();
        try (XMLDecoder entrada = new XMLDecoder(new FileInputStream(ARQUIVO), null, erroLeitura::set)) {
            EstadoPersistido estado = EstadoPersistido.class.cast(entrada.readObject());
            if (erroLeitura.get() != null) {
                throw new BikeShareException("Nao foi possivel carregar os dados do sistema.", erroLeitura.get());
            }
            if (estado == null) {
                throw new BikeShareException("Formato de dados persistidos invalido.");
            }
            return estado;
        } catch (IOException | ClassCastException excecao) {
            throw new BikeShareException("Nao foi possivel carregar os dados do sistema.", excecao);
        }
    }

    public void salvar(EstadoPersistido estado) {
        AtomicReference<Exception> erroEscrita = new AtomicReference<>();
        try (XMLEncoder saida = new XMLEncoder(new FileOutputStream(ARQUIVO_TEMPORARIO))) {
            saida.setExceptionListener(erroEscrita::set);
            configurarPersistenciaXml(saida);
            saida.writeObject(estado);
        } catch (IOException excecao) {
            throw new BikeShareException("Nao foi possivel salvar os dados do sistema.", excecao);
        }
        if (erroEscrita.get() != null) {
            throw new BikeShareException("Nao foi possivel salvar os dados do sistema.", erroEscrita.get());
        }
        try {
            Files.move(ARQUIVO_TEMPORARIO.toPath(), ARQUIVO.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException excecao) {
            throw new BikeShareException("Nao foi possivel salvar os dados do sistema.", excecao);
        }
    }

    private void configurarPersistenciaXml(XMLEncoder encoder) {
        registrarEstadoPersistido(encoder);
        registrarPessoaFisica(encoder);
        registrarPessoaJuridica(encoder);
        registrarPlanos(encoder);
        registrarMetodosPagamento(encoder);
        registrarBigDecimal(encoder);
        registrarEstacao(encoder);
        registrarBicicletas(encoder);
        registrarAluguel(encoder);
    }

    private void registrarEstadoPersistido(XMLEncoder encoder) {
        encoder.setPersistenceDelegate(EstadoPersistido.class,
                delegate(EstadoPersistido.class, objeto -> {
                    EstadoPersistido estado = (EstadoPersistido) objeto;
                    return new Object[]{estado.getUsuarios(), estado.getProximoIdUsuario(),
                            estado.getEstacoes(), estado.getProximoIdEstacao(), estado.getBicicletas(),
                            estado.getAlugueis(), estado.getProximoIdAluguel()};
                }));
    }

    private void registrarEstacao(XMLEncoder encoder) {
        encoder.setPersistenceDelegate(Estacao.class,
                delegate(Estacao.class, objeto -> {
                    Estacao estacao = (Estacao) objeto;
                    return new Object[]{estacao.getId(), estacao.getNome(), estacao.getEndereco(),
                            estacao.getCapacidade(), estacao.getQuantidadeBicicletas()};
                }));
    }

    private void registrarBicicletas(XMLEncoder encoder) {
        encoder.setPersistenceDelegate(BicicletaConvencional.class,
                delegate(BicicletaConvencional.class, objeto -> {
                    BicicletaConvencional bicicleta = (BicicletaConvencional) objeto;
                    return new Object[]{bicicleta.getId(), bicicleta.getEstacao(), bicicleta.getEstado()};
                }));
        encoder.setPersistenceDelegate(BicicletaEletrica.class,
                delegate(BicicletaEletrica.class, objeto -> {
                    BicicletaEletrica bicicleta = (BicicletaEletrica) objeto;
                    return new Object[]{bicicleta.getId(), bicicleta.getEstacao(), bicicleta.getEstado(),
                            bicicleta.getNivelBateria()};
                }));
    }

    private void registrarAluguel(XMLEncoder encoder) {
        encoder.setPersistenceDelegate(Aluguel.class,
                delegate(Aluguel.class, objeto -> {
                    Aluguel aluguel = (Aluguel) objeto;
                    return new Object[]{aluguel.getId(), aluguel.getUsuario(), aluguel.getBicicleta(),
                            aluguel.getEstacaoOrigem(), aluguel.getInicioFormatado(), aluguel.getEstado(),
                            aluguel.getEstacaoDestino(), aluguel.getFimFormatado(),
                            aluguel.getValorCobrado() == null ? null : aluguel.getValorCobrado().toPlainString()};
                }));
    }

    private void registrarPessoaFisica(XMLEncoder encoder) {
        encoder.setPersistenceDelegate(PessoaFisica.class,
                delegate(PessoaFisica.class, objeto -> {
                    PessoaFisica usuario = (PessoaFisica) objeto;
                    return new Object[]{usuario.getId(), usuario.getNome(), usuario.getEmail(), usuario.getSenha(),
                            usuario.getCpf(), usuario.getPlano(), usuario.getMetodoPagamento(),
                            usuario.getSaldoPreCarregado(), usuario.getAluguelAtivo(),
                            usuario.getTotalPendencias()};
                }));
    }

    private void registrarPessoaJuridica(XMLEncoder encoder) {
        encoder.setPersistenceDelegate(PessoaJuridica.class,
                delegate(PessoaJuridica.class, objeto -> {
                    PessoaJuridica usuario = (PessoaJuridica) objeto;
                    return new Object[]{usuario.getId(), usuario.getNome(), usuario.getEmail(), usuario.getSenha(),
                            usuario.getCnpj(), usuario.getRazaoSocial(), usuario.getPlano(),
                            usuario.getMetodoPagamento(), usuario.getSaldoPreCarregado(), usuario.getAluguelAtivo(),
                            usuario.getTotalPendencias()};
                }));
    }

    private void registrarPlanos(XMLEncoder encoder) {
        encoder.setPersistenceDelegate(PlanoAvulso.class, delegate(PlanoAvulso.class, objeto -> new Object[]{}));
        encoder.setPersistenceDelegate(PlanoAssinante.class,
                delegate(PlanoAssinante.class, objeto -> new Object[]{((PlanoAssinante) objeto).getMensalidade()}));
    }

    private void registrarMetodosPagamento(XMLEncoder encoder) {
        encoder.setPersistenceDelegate(Cartao.class,
                delegate(Cartao.class, objeto -> {
                    Cartao cartao = (Cartao) objeto;
                    return new Object[]{cartao.getNumero(), cartao.getValidade()};
                }));
        encoder.setPersistenceDelegate(Pix.class,
                delegate(Pix.class, objeto -> new Object[]{((Pix) objeto).getChave()}));
        encoder.setPersistenceDelegate(SaldoPreCarregado.class,
                delegate(SaldoPreCarregado.class, objeto -> new Object[]{((SaldoPreCarregado) objeto).getSaldo()}));
    }

    private void registrarBigDecimal(XMLEncoder encoder) {
        encoder.setPersistenceDelegate(BigDecimal.class,
                delegate(BigDecimal.class, objeto -> new Object[]{objeto.toString()}));
    }

    private PersistenceDelegate delegate(Class<?> tipo, Function<Object, Object[]> argumentos) {
        return new PersistenceDelegate() {
            @Override
            protected Expression instantiate(Object objeto, java.beans.Encoder encoder) {
                return new Expression(objeto, tipo, "new", argumentos.apply(objeto));
            }

            @Override
            protected void initialize(Class<?> classe, Object origem, Object destino,
                                      java.beans.Encoder encoder) {
            }
        };
    }
}
