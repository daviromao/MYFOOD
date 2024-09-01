package br.ufal.ic.p2.myfood.models;

import br.ufal.ic.p2.myfood.exceptions.AtributoInvalidoException;
import br.ufal.ic.p2.myfood.persistence.Persistente;

import java.util.Locale;

public class Produto implements Persistente {
    private int id;
    private String nome;
    private float valor;
    private String categoria;
    private Empresa empresa;

    public Produto() {
    }

    public Produto(String nome, float valor, String categoria, Empresa empresa) throws AtributoInvalidoException {
        this.nome = nome;
        this.valor = valor;
        this.categoria = categoria;
        this.empresa = empresa;
        this.validate();
    }

    private void validate() throws AtributoInvalidoException {
        if (nome == null || nome.isBlank())
            throw new AtributoInvalidoException("Nome invalido");

        if (valor < 0)
            throw new AtributoInvalidoException("Valor invalido");

        if (categoria == null || categoria.isBlank())
            throw new AtributoInvalidoException("Categoria invalido");
    }


    public String getAtributo(String atributo) throws AtributoInvalidoException {
        switch (atributo) {
            case "nome":
                return getNome();
            case "valor":
                return String.format(Locale.US, "%.2f", valor);
            case "categoria":
                return getCategoria();
            case "empresa":
                return String.valueOf(empresa.getNome());
            default:
                throw new AtributoInvalidoException("Atributo nao existe");
        }
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public float getValor() {
        return valor;
    }

    public void setValor(float valor) {
        this.valor = valor;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public Empresa setEmpresa() {
        return empresa;
    }
}
