package br.ufal.ic.p2.myfood.exceptions;

public class CredenciaisInvalidasException extends RuntimeException{
    public CredenciaisInvalidasException(){
        super("Login ou senha invalidos");
    }
}
