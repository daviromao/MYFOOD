package br.ufal.ic.p2.myfood.models.users;

import br.ufal.ic.p2.myfood.exceptions.AtributoInvalidoException;
import br.ufal.ic.p2.myfood.models.enums.Permissoes;
import br.ufal.ic.p2.myfood.models.enums.TipoUsuario;
import br.ufal.ic.p2.myfood.persistence.Persistente;

import java.util.List;

public abstract class Usuario implements Persistente {
    private int id;
    private String nome;
    private String email;
    private String senha;
    private String endereco;

    public Usuario() {}

    public Usuario(String nome, String email, String senha, String endereco) throws AtributoInvalidoException {
        if(nome == null || nome.isEmpty())
            throw new AtributoInvalidoException("Nome invalido");

        if(email == null || email.isEmpty() || !email.matches("^(.+)@(\\S+)$"))
            throw new AtributoInvalidoException("Email invalido");

        if(senha == null || senha.isEmpty())
            throw new AtributoInvalidoException("Senha invalido");

        if(endereco == null || endereco.isEmpty())
            throw new AtributoInvalidoException("Endereco invalido");

        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.endereco = endereco;
    }

    public String getAtributo(String atributo) throws AtributoInvalidoException{
        return switch (atributo) {
            case "nome" -> nome;
            case "email" -> email;
            case "senha" -> senha;
            case "endereco" -> endereco;
            default -> throw new AtributoInvalidoException("Atributo invalido");
        };
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getEmail() {
        return email;
    }

    public String getSenha() {
        return senha;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public abstract TipoUsuario getTipo();

    public abstract List<Permissoes> getPermissoes();
}
