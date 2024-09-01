package br.ufal.ic.p2.myfood.persistence;

import br.ufal.ic.p2.myfood.exceptions.ObjetoNaoEncontradoException;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PersistenciaXML<T extends Persistente> implements Persistencia<T> {
    private int COUNT_ID = 0;
    private final Map<Integer, T> objetos = new HashMap<>();
    private final File arquivo;

    public PersistenciaXML(String nomeArquivo) {
        this.arquivo = new File(nomeArquivo);
        loadXML();
    }

    private void loadXML() {
        if(!arquivo.exists())
            return;

        try(XMLDecoder decoder = new XMLDecoder(new FileInputStream(arquivo))) {
            COUNT_ID = (int) decoder.readObject();
            Map<Integer, T> objetos = (Map<Integer, T>) decoder.readObject();
            this.objetos.putAll(objetos);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void flushXML() {
        try (XMLEncoder encoder = new XMLEncoder(new FileOutputStream(arquivo))) {
            encoder.writeObject(COUNT_ID);
            encoder.writeObject(objetos);
            encoder.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public T salvar(T objeto) {
        objeto.setId(COUNT_ID);
        objetos.put(objeto.getId(), objeto);
        COUNT_ID++;
        flushXML();
        return objeto;
    }

    @Override
    public T atualizar(int id, T novoObjeto) throws ObjetoNaoEncontradoException {
        T objetoExistente = buscar(id);
        if(objetoExistente == null)
            throw new ObjetoNaoEncontradoException("Objeto não encontrado");

        objetos.put(id, novoObjeto);
        flushXML();
        return novoObjeto;
    }

    @Override
    public T buscar(int id) {
        return objetos.get(id);
    }

    @Override
    public List<T> buscarTodos() {
        return List.copyOf(objetos.values());
    }

    @Override
    public void deletar(T objeto) {
        objetos.remove(objeto.getId());
        flushXML();
    }

    @Override
    public void deletarTodos() {
        objetos.clear();
        flushXML();
    }
}
