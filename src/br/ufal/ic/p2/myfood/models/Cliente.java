package br.ufal.ic.p2.myfood.models;

import br.ufal.ic.p2.myfood.exceptions.AtributoInvalidoException;

import java.util.List;

public class Cliente extends Usuario {
    public Cliente() {};

    public Cliente(String nome, String email, String senha, String endereco) throws AtributoInvalidoException {
        super(nome, email, senha, endereco);
    }

    @Override
    public String getTipo() {
        return TipoUsuario.CLIENTE.name();
    }

    @Override
    public List<Permissoes> getPermissoes() {
        return List.of(Permissoes.FAZER_PEDIDO);
    }
}
