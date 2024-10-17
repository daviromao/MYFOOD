package br.ufal.ic.p2.myfood.models;

import br.ufal.ic.p2.myfood.exceptions.AtributoInvalidoException;
import br.ufal.ic.p2.myfood.exceptions.EstadoPedidoInvalidoException;
import br.ufal.ic.p2.myfood.exceptions.ObjetoNaoEncontradoException;
import br.ufal.ic.p2.myfood.persistence.Persistente;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Pedido implements Persistente {
    private int id;
    private Cliente cliente;
    private Empresa empresa;
    private EstadoPedido estado;
    private List<Produto> produtos;

    private Entrega entrega;

    public Pedido() {
    }

    public Pedido(Cliente cliente, Empresa empresa, EstadoPedido estado) {
        this.cliente = cliente;
        this.empresa = empresa;
        this.estado = estado;
        this.produtos = new ArrayList<>();
    }

    public float getValorTotal() {
        return produtos.stream().map(Produto::getValor).reduce(0f, Float::sum);
    }

    public List<String> getNomeProdutos() {
        List<String> nomes = new ArrayList<>();
        for (Produto produto : produtos) {
            nomes.add(produto.getNome());
        }
        return nomes;
    }

    public String getAtributo(String atributo) throws AtributoInvalidoException {
        if (atributo == null || atributo.isEmpty())
            throw new AtributoInvalidoException("Atributo invalido");

        return switch (atributo) {
            case "cliente" -> String.valueOf(cliente.getNome());
            case "empresa" -> String.valueOf(empresa.getNome());
            case "estado" -> getEstado().estado;
            case "valor" -> String.format(Locale.US, "%.2f", getValorTotal());
            case "produtos" -> "{" + getNomeProdutos() + "}";
            default -> throw new AtributoInvalidoException("Atributo nao existe");
        };
    }

    public void adicionarProduto(Produto produto) throws EstadoPedidoInvalidoException, ObjetoNaoEncontradoException {
        if(!estado.equals(EstadoPedido.ABERTO))
            throw new EstadoPedidoInvalidoException("Nao e possivel adcionar produtos a um pedido fechado");

        if(!empresa.equals(produto.getEmpresa()))
            throw new ObjetoNaoEncontradoException("O produto nao pertence a essa empresa");

        produtos.add(produto);
    }

    public void removerProduto(String produto) throws ObjetoNaoEncontradoException, AtributoInvalidoException, EstadoPedidoInvalidoException {
        if (!estado.equals(EstadoPedido.ABERTO))
            throw new EstadoPedidoInvalidoException("Nao e possivel remover produtos de um pedido fechado");

        if (produto == null || produto.isEmpty())
            throw new AtributoInvalidoException("Produto invalido");

        for (Produto p : produtos) {
            if (p.getNome().equals(produto)) {
                produtos.remove(p);
                return;
            }
        }
        throw new ObjetoNaoEncontradoException("Produto nao encontrado");
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public EstadoPedido getEstado() {
        return estado;
    }

    public void setEstado(EstadoPedido estado) {
        this.estado = estado;
    }

    public List<Produto> getProdutos() {
        return produtos;
    }

    public void setProdutos(List<Produto> produtos) {
        this.produtos = produtos;
    }

    public Entrega getEntrega() {
        return entrega;
    }

    public void setEntrega(Entrega entrega) {
        this.entrega = entrega;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }
}
