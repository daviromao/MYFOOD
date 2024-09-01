package br.ufal.ic.p2.myfood.services;

import br.ufal.ic.p2.myfood.exceptions.ObjetoNaoEncontradoException;
import br.ufal.ic.p2.myfood.exceptions.ProdutoDuplicadoException;
import br.ufal.ic.p2.myfood.models.Produto;
import br.ufal.ic.p2.myfood.persistence.Persistencia;
import br.ufal.ic.p2.myfood.persistence.PersistenciaXML;

public class ProdutoService {
    private final Persistencia<Produto> produtos = new PersistenciaXML<>("db/produtos.xml");

    public void salvar(Produto produto) throws ProdutoDuplicadoException {
        for(Produto p : produtos.buscarTodos()) {
            if(p.getNome().equals(produto.getNome())) {
                if(p.getEmpresa().equals(produto.getEmpresa())) {
                    throw new ProdutoDuplicadoException();
                }
            }
        }

        produtos.salvar(produto);
    }

    public Produto buscar(int idProduto) throws ObjetoNaoEncontradoException {
        Produto produto = produtos.buscar(idProduto);
        if(produto == null)
            throw new ObjetoNaoEncontradoException("Produto nao cadastrado");
        return produto;
    }

    public Produto editarProduto(int idProduto, Produto novoProduto) throws ObjetoNaoEncontradoException {
        Produto produto = buscar(idProduto);

        produto.setCategoria(novoProduto.getCategoria());
        produto.setNome(novoProduto.getNome());
        produto.setValor(novoProduto.getValor());

        return produtos.atualizar(idProduto, produto);
    }

    public void deletarTodos(){
        produtos.deletarTodos();
    }

    public void salvarTodos(){
        produtos.salvarTodos();
    }
}
