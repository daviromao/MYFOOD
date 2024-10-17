package br.ufal.ic.p2.myfood.services;

import br.ufal.ic.p2.myfood.exceptions.ObjetoNaoEncontradoException;
import br.ufal.ic.p2.myfood.models.pedidos.Pedido;
import br.ufal.ic.p2.myfood.models.enums.EntregadorStatus;
import br.ufal.ic.p2.myfood.models.enums.EstadoPedido;
import br.ufal.ic.p2.myfood.models.pedidos.Entrega;
import br.ufal.ic.p2.myfood.models.users.Entregador;
import br.ufal.ic.p2.myfood.persistence.Persistencia;
import br.ufal.ic.p2.myfood.persistence.PersistenciaXML;

public class EntregaService {
    private final Persistencia<Entrega> entregas = new PersistenciaXML<>("db/entregas.xml");

    public Entrega criarEntrega(Pedido pedido, Entregador entregador, String destino) {
        if(destino == null || destino.isEmpty())
            destino = pedido.getCliente().getEndereco();

        Entrega entrega = new Entrega(pedido, entregador, destino);
        pedido.setEstado(EstadoPedido.ENTREGANDO);
        pedido.setEntrega(entrega);
        entregador.setStatus(EntregadorStatus.OCUPADO);
        return entregas.salvar(entrega);
    }

    public void finalizarEntrega(int idEntrega) throws ObjetoNaoEncontradoException {
        Entrega entrega = entregas.buscar(idEntrega);

        if(entrega == null)
            throw new ObjetoNaoEncontradoException("Nao existe nada para ser entregue com esse id");

        entrega.getPedido().setEstado(EstadoPedido.ENTREGUE);
        entrega.getEntregador().setStatus(EntregadorStatus.DISPONIVEL);
    }

    public Entrega buscar(int idEntrega) {
        return entregas.buscar(idEntrega);
    }
}
