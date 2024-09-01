package br.ufal.ic.p2.myfood.exceptions;

public class PedidoDuplicadoException extends Exception {
    public PedidoDuplicadoException() {
        super("Nao e permitido ter dois pedidos em aberto para a mesma empresa");
    }
}