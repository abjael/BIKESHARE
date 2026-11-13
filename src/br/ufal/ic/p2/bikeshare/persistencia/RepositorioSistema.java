package br.ufal.ic.p2.bikeshare.persistencia;

import br.ufal.ic.p2.bikeshare.excecao.BikeShareException;
import br.ufal.ic.p2.bikeshare.dominio.PessoaFisica;
import br.ufal.ic.p2.bikeshare.dominio.PessoaJuridica;
import br.ufal.ic.p2.bikeshare.dominio.PlanoAssinante;
import br.ufal.ic.p2.bikeshare.dominio.PlanoAvulso;

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
            return new EstadoPersistido(java.util.Collections.emptyMap(), 1);
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
        registrarBigDecimal(encoder);
    }

    private void registrarEstadoPersistido(XMLEncoder encoder) {
        encoder.setPersistenceDelegate(EstadoPersistido.class,
                delegate(EstadoPersistido.class, objeto -> {
                    EstadoPersistido estado = (EstadoPersistido) objeto;
                    return new Object[]{estado.getUsuarios(), estado.getProximoIdUsuario()};
                }));
    }

    private void registrarPessoaFisica(XMLEncoder encoder) {
        encoder.setPersistenceDelegate(PessoaFisica.class,
                delegate(PessoaFisica.class, objeto -> {
                    PessoaFisica usuario = (PessoaFisica) objeto;
                    return new Object[]{usuario.getId(), usuario.getNome(), usuario.getEmail(), usuario.getSenha(),
                            usuario.getCpf(), usuario.getPlano()};
                }));
    }

    private void registrarPessoaJuridica(XMLEncoder encoder) {
        encoder.setPersistenceDelegate(PessoaJuridica.class,
                delegate(PessoaJuridica.class, objeto -> {
                    PessoaJuridica usuario = (PessoaJuridica) objeto;
                    return new Object[]{usuario.getId(), usuario.getNome(), usuario.getEmail(), usuario.getSenha(),
                            usuario.getCnpj(), usuario.getRazaoSocial(), usuario.getPlano()};
                }));
    }

    private void registrarPlanos(XMLEncoder encoder) {
        encoder.setPersistenceDelegate(PlanoAvulso.class, delegate(PlanoAvulso.class, objeto -> new Object[]{}));
        encoder.setPersistenceDelegate(PlanoAssinante.class,
                delegate(PlanoAssinante.class, objeto -> new Object[]{((PlanoAssinante) objeto).getMensalidade()}));
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
