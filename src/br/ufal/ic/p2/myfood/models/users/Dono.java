package br.ufal.ic.p2.myfood.models.users;

import br.ufal.ic.p2.myfood.exceptions.AtributoInvalidoException;
import br.ufal.ic.p2.myfood.models.enums.Permissoes;
import br.ufal.ic.p2.myfood.models.enums.TipoUsuario;

import java.util.List;

public class Dono extends Usuario {
    private String cpf;

    public Dono() {
    }

    public Dono(String nome, String email, String senha, String endereco, String cpf) throws AtributoInvalidoException {
        super(nome, email, senha, endereco);
        if (cpf == null || cpf.isEmpty() || !cpf.matches("^\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}$"))
            throw new AtributoInvalidoException("CPF invalido");

        this.cpf = cpf;
    }

    public String getAtributo(String atributo) throws AtributoInvalidoException {
        return switch (atributo) {
            case "cpf" -> cpf;
            default -> super.getAtributo(atributo);
        };
    }

    public TipoUsuario getTipo() {
        return TipoUsuario.DONO;
    }

    @Override
    public List<Permissoes> getPermissoes() {
        return List.of(Permissoes.CRIAR_EMRPESA);
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }
}
