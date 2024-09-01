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

//    public int criarPedido(int cliente, int empresa) {
//        User client = users.find(cliente);
//        Company company = companies.find(empresa);
//
//        if (client instanceof Owner)
//            throw new RuntimeException("Dono de empresa nao pode fazer um pedido");
//
//        orders.findAll()
//                .stream()
//                .filter(order -> order.getClientId() == cliente && order.getCompanyId() == empresa && order.getStatus().equals("aberto"))
//                .findAny()
//                .ifPresent(order -> {
//                    throw new RuntimeException("Nao e permitido ter dois pedidos em aberto para a mesma empresa");
//                });
//
//        Order order = orders.create(new Order(cliente, empresa));
//        order.setStatus("aberto");
//
//        return order.getId();
//    }
//
//    public int getNumeroPedido(int cliente, int empresa, int indice) {
//        List<Order> orders = this.orders.findAll()
//                .stream()
//                .filter(order -> order.getClientId() == cliente && order.getCompanyId() == empresa)
//                .toList();
//
//        return orders.get(indice).getId();
//    }
//
//    public void adicionarProduto(int pedido, int produto) {
//        Order order = orders.find(pedido);
//        Product product = products.find(produto);
//
//        if (order == null)
//            throw new ObjetoNaoEncontradoException("Nao existe pedido em aberto");
//
//        if (product.getCompanyId() != order.getCompanyId())
//            throw new RuntimeException("O produto nao pertence a essa empresa");
//
//        if (!order.getStatus().equals("aberto"))
//            throw new RuntimeException("Nao e possivel adcionar produtos a um pedido fechado");
//
//        order.getProductsId().add(product.getId());
//    }
//
//    public String getPedidos(int numero, String atributo) {
//        Order order = orders.find(numero);
//
//        if (atributo == null || atributo.isEmpty())
//            throw new RuntimeException("Atributo invalido");
//
//        return switch (atributo) {
//            case "cliente" -> users.find(order.getClientId()).getName();
//            case "empresa" -> companies.find(order.getCompanyId()).getName();
//            case "estado" -> order.getStatus();
//            case "produtos" ->
//                    "{" + order.getProductsId().stream().map(id -> products.find(id).getName()).toList() + "}";
//            case "valor" -> {
//                double valor = order.getProductsId().stream().mapToDouble(id -> products.find(id).getPrice()).sum();
//                yield String.format(Locale.US, "%.2f", valor);
//            }
//            default -> throw new RuntimeException("Atributo nao existe");
//        };
//    }
//
//    public void fecharPedido(int numero) {
//        Order order = orders.find(numero);
//
//        if (order == null)
//            throw new ObjetoNaoEncontradoException("Pedido nao encontrado");
//
//        order.setStatus("preparando");
//        orders.update(numero, order);
//    }
//
//    public void removerProduto(int pedido, String produto) {
//        Order order = orders.find(pedido);
//
//        if (produto == null || produto.isEmpty())
//            throw new RuntimeException("Produto invalido");
//
//        if (order == null)
//            throw new ObjetoNaoEncontradoException("Pedido nao encontrado");
//
//        if (!order.getStatus().equals("aberto"))
//            throw new RuntimeException("Nao e possivel remover produtos de um pedido fechado");
//
//        products.findAll()
//                .stream()
//                .filter(p -> p.getName().equals(produto) && order.getProductsId().contains(p.getId()))
//                .findFirst()
//                .ifPresentOrElse(
//                        product -> order.getProductsId().remove((Object) product.getId()),
//                        () -> {
//                            throw new RuntimeException("Produto nao encontrado");
//                        }
//                );
//    }
//
    public void encerrarSistema() {
        sistema.encerrarSistema();
    }

}
