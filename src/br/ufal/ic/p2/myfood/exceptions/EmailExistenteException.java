package br.ufal.ic.p2.myfood.exceptions;

public class EmailExistenteException extends Exception {
    public EmailExistenteException() {
        super("Conta com esse email ja existe");
    }
}
