package br.ufal.ic.p2.myfood.models;

public enum EstadoPedido {
    ABERTO("aberto"), PREPARANDO("preparando"), PRONTO("pronto"), ENTREGANDO("entregando"), ENTREGUE("entregue");

    public final String estado;
    EstadoPedido(String estado) {
        this.estado = estado;
    }
}
