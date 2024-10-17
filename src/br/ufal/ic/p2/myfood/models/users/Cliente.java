package br.ufal.ic.p2.myfood.models.users;

import br.ufal.ic.p2.myfood.exceptions.AtributoInvalidoException;
import br.ufal.ic.p2.myfood.models.enums.Permissoes;
import br.ufal.ic.p2.myfood.models.enums.TipoUsuario;

import java.util.List;

public class Cliente extends Usuario {
    public Cliente() {};

    public Cliente(String nome, String email, String senha, String endereco) throws AtributoInvalidoException {
        super(nome, email, senha, endereco);
    }

    @Override
    public TipoUsuario getTipo() {
        return TipoUsuario.CLIENTE;
    }

    @Override
    public List<Permissoes> getPermissoes() {
        return List.of(Permissoes.FAZER_PEDIDO);
    }
}
