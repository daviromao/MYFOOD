package br.ufal.ic.p2.myfood;


import br.ufal.ic.p2.myfood.exceptions.*;


public class Facade {

    private Sistema sistema = Sistema.getInstance();
    //TODO: REMOVE XMLS
    public void zerarSistema() {
        sistema.zerarSistema();
    }

    public String getAtributoUsuario(int id, String nome) throws Exception {
        return sistema.getAtributoUsuario(id, nome);
    }

    public void criarUsuario(String nome, String email, String senha, String endereco) throws AtributoInvalidoException, EmailExistenteException {
        sistema.criarUsuario(nome, email, senha, endereco);
    }

    public void criarUsuario(String nome, String email, String senha, String endereco, String cpf) throws AtributoInvalidoException, EmailExistenteException {
        sistema.criarUsuario(nome, email, senha, endereco, cpf);
    }

    public int login(String email, String senha) {
        return sistema.login(email, senha);
    }

    public int criarEmpresa(String tipoEmpresa, int dono, String nome, String endereco, String tipoCozinha) throws AtributoInvalidoException, EmpresaInvalidaException, ObjetoNaoEncontradoException, UsuarioSemPermissaoException {
        return sistema.criarEmpresa(tipoEmpresa, dono, nome, endereco, tipoCozinha);
    }

    public String getEmpresasDoUsuario(int idDono) throws ObjetoNaoEncontradoException, UsuarioSemPermissaoException {
        return sistema.getEmpresasDoUsuario(idDono);
    }

    public int getIdEmpresa(int idDono, String nome, int indice) throws AtributoInvalidoException, ObjetoNaoEncontradoException, UsuarioSemPermissaoException {
        return sistema.getIdEmpresa(idDono, nome, indice);
    }

    public String getAtributoEmpresa(int empresa, String atributo) throws ObjetoNaoEncontradoException, AtributoInvalidoException {
        return sistema.getEmpresa(empresa).getAtributo(atributo);
    }

    public int criarProduto(int empresa, String nome, float valor, String categoria) throws AtributoInvalidoException, ObjetoNaoEncontradoException, ProdutoDuplicadoException {
        return sistema.criarProduto(empresa, nome, valor, categoria);
    }

    public void editarProduto(int produtoId, String nome, float valor, String categoria) throws AtributoInvalidoException, ObjetoNaoEncontradoException {
        sistema.editarProduto(produtoId, nome, valor, categoria);
    }

    public String getProduto(String nome, int empresa, String atributo) throws AtributoInvalidoException, ObjetoNaoEncontradoException {
        return sistema.getProduto(nome, empresa, atributo);
    }

    public String listarProdutos(int empresa) throws ObjetoNaoEncontradoException {
        return sistema.listarProdutos(empresa);
    }

    public int criarPedido(int cliente, int empresa) throws ObjetoNaoEncontradoException, UsuarioSemPermissaoException, PedidoDuplicadoException {
        return sistema.criarPedido(cliente, empresa);
    }

    public void adicionarProduto(Integer pedido, Integer produto) throws ObjetoNaoEncontradoException, EstadoPedidoInvalidoException {
        sistema.adicionarProduto(pedido, produto);
    }

    public String getPedidos(int numero, String atributo) throws ObjetoNaoEncontradoException, AtributoInvalidoException {
        return sistema.getPedido(numero, atributo);
    }

    public void fecharPedido(int numero) throws ObjetoNaoEncontradoException {
        sistema.fecharPedido(numero);
    }

    public void removerProduto(int pedido, String produto) throws ObjetoNaoEncontradoException, AtributoInvalidoException, EstadoPedidoInvalidoException {
        sistema.removerProduto(pedido, produto);
    }

    public int getNumeroPedido(int cliente, int empresa, int indice) throws AtributoInvalidoException, ObjetoNaoEncontradoException {
        return sistema.getNumeroPedido(cliente, empresa, indice);
    }

    public void encerrarSistema() {
        sistema.encerrarSistema();
    }

}
