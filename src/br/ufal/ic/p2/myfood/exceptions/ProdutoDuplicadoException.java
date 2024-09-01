package br.ufal.ic.p2.myfood.exceptions;

public class ProdutoDuplicadoException extends Exception {
    public ProdutoDuplicadoException() {
        super("Ja existe um produto com esse nome para essa empresa");
    }
}
