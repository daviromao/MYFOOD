package br.ufal.ic.p2.myfood.services;

import br.ufal.ic.p2.myfood.exceptions.AtributoInvalidoException;
import br.ufal.ic.p2.myfood.exceptions.EstadoPedidoInvalidoException;
import br.ufal.ic.p2.myfood.exceptions.ObjetoNaoEncontradoException;
import br.ufal.ic.p2.myfood.exceptions.PedidoDuplicadoException;
import br.ufal.ic.p2.myfood.models.Cliente;
import br.ufal.ic.p2.myfood.models.Empresa;
import br.ufal.ic.p2.myfood.models.Pedido;
import br.ufal.ic.p2.myfood.models.Produto;
import br.ufal.ic.p2.myfood.persistence.Persistencia;
import br.ufal.ic.p2.myfood.persistence.PersistenciaXML;

import java.util.ArrayList;
import java.util.List;

public class PedidoService {
    private final Persistencia<Pedido> pedidos = new PersistenciaXML<>("db/pedidos.xml");

    public int criarPedido(Cliente cliente, Empresa empresa) throws PedidoDuplicadoException {

        for (Pedido pedido : pedidos.buscarTodos()) {
            if (pedido.getCliente().equals(cliente) &&
                pedido.getEmpresa().equals(empresa) &&
                pedido.getEstado().equals("aberto")) {
                throw new PedidoDuplicadoException();
            }
        }

        Pedido novoPedido = new Pedido(cliente, empresa, "aberto");

        pedidos.salvar(novoPedido);
        return novoPedido.getId();
    }

    public void adicionarProduto(int idPedido, Produto produto) throws ObjetoNaoEncontradoException, EstadoPedidoInvalidoException {
        Pedido pedido = pedidos.buscar(idPedido);

        if (pedido == null)
            throw new ObjetoNaoEncontradoException("Nao existe pedido em aberto");

        if(!pedido.getEstado().equals("aberto"))
            throw new EstadoPedidoInvalidoException("Nao e possivel adcionar produtos a um pedido fechado");

        if(!pedido.getEmpresa().equals(produto.getEmpresa()))
            throw new ObjetoNaoEncontradoException("O produto nao pertence a essa empresa");

        pedido.adicionarProduto(produto);
        pedidos.atualizar(idPedido, pedido);
    }

    public void removerProduto(int idPedido, String produto) throws ObjetoNaoEncontradoException, AtributoInvalidoException, EstadoPedidoInvalidoException {
        Pedido pedido = pedidos.buscar(idPedido);

        if(pedido == null)
            throw new ObjetoNaoEncontradoException("Pedido nao encontrado");

        pedido.removerProduto(produto);
        pedidos.atualizar(idPedido, pedido);
    }

    public void fecharPedido(int idPedido) throws ObjetoNaoEncontradoException {
        Pedido pedido = pedidos.buscar(idPedido);

        if(pedido == null)
            throw new ObjetoNaoEncontradoException("Pedido nao encontrado");

        pedido.setEstado("preparando");
        pedidos.atualizar(idPedido, pedido);
    }

    public String getPedidos(int numero, String atributo) throws AtributoInvalidoException {
        Pedido pedido = pedidos.buscar(numero);
        return pedido.getAtributo(atributo);
    }

    public int getNumeroPedido(Cliente cliente, Empresa empresa, int indice) throws AtributoInvalidoException {

        List<Pedido> pedidosDoCliente = new ArrayList<>();

        for(Pedido pedido : pedidos.buscarTodos()) {
            if(pedido.getCliente().getId() == cliente.getId() && pedido.getEmpresa().getId() == empresa.getId())
                    pedidosDoCliente.add(pedido);
        }

        if(indice < 0 || indice >= pedidosDoCliente.size())
            throw new AtributoInvalidoException("Indice invalido");

        return pedidosDoCliente.get(indice).getId();
    }

    public Pedido buscarPedido(int numero) {
        Pedido pedido = pedidos.buscar(numero);

        return pedido;
    }

    public void deletarTodos() {
        pedidos.deletarTodos();
    }

    public void salvarTodos() {
        pedidos.salvarTodos();
    }
}
