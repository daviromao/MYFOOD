package br.ufal.ic.p2.myfood.services;

import br.ufal.ic.p2.myfood.exceptions.AtributoInvalidoException;
import br.ufal.ic.p2.myfood.exceptions.EstadoPedidoInvalidoException;
import br.ufal.ic.p2.myfood.exceptions.ObjetoNaoEncontradoException;
import br.ufal.ic.p2.myfood.exceptions.PedidoDuplicadoException;
import br.ufal.ic.p2.myfood.models.pedidos.Pedido;
import br.ufal.ic.p2.myfood.models.enums.EstadoPedido;
import br.ufal.ic.p2.myfood.models.pedidos.Produto;
import br.ufal.ic.p2.myfood.models.users.Cliente;
import br.ufal.ic.p2.myfood.models.empresas.Empresa;
import br.ufal.ic.p2.myfood.models.users.Entregador;
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
                pedido.getEstado().equals(EstadoPedido.ABERTO)) {
                throw new PedidoDuplicadoException();
            }
        }

        Pedido novoPedido = new Pedido(cliente, empresa, EstadoPedido.ABERTO);

        pedidos.salvar(novoPedido);
        return novoPedido.getId();
    }

    public void adicionarProduto(int idPedido, Produto produto) throws ObjetoNaoEncontradoException, EstadoPedidoInvalidoException {
        Pedido pedido = pedidos.buscar(idPedido);

        if (pedido == null)
            throw new ObjetoNaoEncontradoException("Nao existe pedido em aberto");

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

        pedido.setEstado(EstadoPedido.PREPARANDO);
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

    public Pedido buscarPedidoPorEntregador(Entregador entregador){
        List<Pedido> pedidosList = pedidos
                .buscarTodos()
                .stream()
                .filter(
                    pedido -> pedido.getEmpresa().getEntregadores().contains(entregador) &&
                            pedido.getEstado().equals(EstadoPedido.PRONTO))
                .toList();

        List<Pedido> prioridade = pedidosList
                .stream()
                .filter(pedido -> pedido.getEmpresa().getTipoEmpresa().equals("farmacia"))
                .toList();
        if(prioridade.size() > 0)
            return prioridade.get(0);

        if(pedidosList.size() > 0)
            return pedidosList.get(0);

        return null;
    }

    public void deletarTodos() {
        pedidos.deletarTodos();
    }

    public void salvarTodos() {
        pedidos.salvarTodos();
    }
}
