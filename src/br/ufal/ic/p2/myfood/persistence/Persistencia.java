package br.ufal.ic.p2.myfood.persistence;

import br.ufal.ic.p2.myfood.exceptions.ObjetoNaoEncontradoException;

import java.util.List;

public interface Persistencia<T extends Persistente> {

    T salvar(T object);
    T atualizar(int id, T object) throws ObjetoNaoEncontradoException;
    T buscar(int id);
    List<T> buscarTodos();
    void deletar(T object);

    void deletarTodos();
}
