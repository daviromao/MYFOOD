package br.ufal.ic.p2.myfood.models;

import br.ufal.ic.p2.myfood.exceptions.AtributoInvalidoException;
import br.ufal.ic.p2.myfood.exceptions.ObjetoNaoEncontradoException;
import br.ufal.ic.p2.myfood.persistence.Persistente;

import java.util.ArrayList;
import java.util.List;

public class Empresa implements Persistente {
    private int id;
    private Dono dono;
    private String nome;
    private String endereco;
    private String tipoEmpresa;

    private List<Produto> produtos;
    private List<Entregador> entregadores;

    public Empresa() {
    }

    ;

    public Empresa(String tipoEmpresa, Dono dono, String nome, String endereco) throws AtributoInvalidoException {
        if (tipoEmpresa == null || tipoEmpresa.isEmpty())
            throw new AtributoInvalidoException("Tipo de empresa invalido");

        if (nome == null || nome.isEmpty())
            throw new AtributoInvalidoException("Nome invalido");

        if (endereco == null || endereco.isEmpty())
            throw new AtributoInvalidoException("Endereco da empresa invalido");

        if (dono == null)
            throw new AtributoInvalidoException("Dono invalido");

        this.tipoEmpresa = tipoEmpresa;
        this.dono = dono;
        this.nome = nome;
        this.endereco = endereco;
        this.produtos = new ArrayList<>();
        this.entregadores = new ArrayList<>();
    }

    public void adicionarProduto(Produto produto) {
        produtos.add(produto);
    }

    public void adicionarEntregador(Entregador entregador) {
        entregadores.add(entregador);
        entregador.adicionarEmpresa(this);
    }

    public Produto getProdutoPeloNome(String nome) throws ObjetoNaoEncontradoException {
        for (Produto produto : produtos) {
            if (produto.getNome().equals(nome)) {
                return produto;
            }
        }

        throw new ObjetoNaoEncontradoException("Produto nao encontrado");
    }

    public String getAtributo(String atributo) throws AtributoInvalidoException {
        if (atributo == null)
            throw new AtributoInvalidoException("Atributo invalido");

        return switch (atributo) {
            case "nome" -> nome;
            case "endereco" -> endereco;
            case "dono" -> dono.getAtributo("nome");
            default -> throw new AtributoInvalidoException("Atributo invalido");
        };
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public Dono getDono() {
        return dono;
    }

    public void setDono(Dono dono) {
        this.dono = dono;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getTipoEmpresa() {
        return tipoEmpresa;
    }

    public void setTipoEmpresa(String tipoEmpresa) {
        this.tipoEmpresa = tipoEmpresa;
    }

    public List<Entregador> getEntregadores() {
        return entregadores;
    }

    public void setEntregadores(List<Entregador> entregadores) {
        this.entregadores = entregadores;
    }

    public List<Produto> getProdutos() {
        return produtos;
    }

    public void setProdutos(List<Produto> produtos) {
        this.produtos = produtos;
    }
}
