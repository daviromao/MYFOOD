package br.ufal.ic.p2.myfood.exceptions;

public class UsuarioSemPermissaoException extends Exception{
    public UsuarioSemPermissaoException(String message) {
        super(message);
    }
}
