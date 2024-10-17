package br.ufal.ic.p2.myfood.models.pedidos;

import br.ufal.ic.p2.myfood.exceptions.AtributoInvalidoException;
import br.ufal.ic.p2.myfood.models.users.Entregador;
import br.ufal.ic.p2.myfood.persistence.Persistente;

public class Entrega implements Persistente {
    private int id;
    private Pedido pedido;
    private Entregador entregador;
    private String destino;

    public Entrega() {
    }

    public Entrega(Pedido pedido, Entregador entregador, String destino) {
        this.pedido = pedido;
        this.entregador = entregador;
        this.destino = destino;
    }

    public String getAtributo(String atributo) throws AtributoInvalidoException {

        if (atributo == null || atributo.isEmpty())
            throw new IllegalArgumentException("Atributo invalido");

        return switch (atributo) {
            case "id" -> String.valueOf(id);
            case "cliente" -> pedido.getCliente().getNome();
            case "destino" -> destino;
            case "empresa" -> pedido.getEmpresa().getNome();
            case "pedido" -> String.valueOf(pedido.getId());
            case "entregador" -> entregador.getNome();
            case "produtos" -> "{" + pedido.getProdutos().stream().map(Produto::getNome).toList() + "}";
            default -> throw new AtributoInvalidoException("Atributo nao existe");
        };
    }

    public Pedido getPedido() {
        return pedido;
    }

    public Entregador getEntregador() {
        return entregador;
    }

    public String getDestino() {
        return destino;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public void setEntregador(Entregador entregador) {
        this.entregador = entregador;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }
}
