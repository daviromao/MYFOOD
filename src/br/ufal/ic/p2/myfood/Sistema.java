package br.ufal.ic.p2.myfood;

import br.ufal.ic.p2.myfood.exceptions.*;
import br.ufal.ic.p2.myfood.models.*;
import br.ufal.ic.p2.myfood.services.EmpresaService;
import br.ufal.ic.p2.myfood.services.PedidoService;
import br.ufal.ic.p2.myfood.services.ProdutoService;
import br.ufal.ic.p2.myfood.services.UsuarioService;

import java.util.List;

public class Sistema {
    private static Sistema instance;

    private UsuarioService usuarioService = new UsuarioService();
    private EmpresaService empresaService = new EmpresaService();
    private ProdutoService produtoService = new ProdutoService();
    private PedidoService pedidoService = new PedidoService();

    private Sistema() {
    }

    public void zerarSistema() {
        empresaService.deletarTodos();
        usuarioService.deletarTodos();
        produtoService.deletarTodos();
        pedidoService.deletarTodos();
    }

    public void encerrarSistema(){
        empresaService.salvarTodos();
        usuarioService.salvarTodos();
        produtoService.salvarTodos();
        pedidoService.salvarTodos();
    }

    public static Sistema getInstance() {
        if (instance == null) {
            instance = new Sistema();
        }

        return instance;
    }

    public String getAtributoUsuario(int id, String atributo) throws AtributoInvalidoException, ObjetoNaoEncontradoException {
        Usuario usuario = usuarioService.buscar(id);
        return usuario.getAtributo(atributo);
    }

    public void criarUsuario(String nome, String email, String senha, String endereco) throws AtributoInvalidoException, EmailExistenteException {
        Usuario usuario = new Cliente(nome, email, senha, endereco);
        usuarioService.salvar(usuario);
    }

    public void criarUsuario(String nome, String email, String senha, String endereco, String cpf) throws AtributoInvalidoException, EmailExistenteException {
        Usuario usuario = new Dono(nome, email, senha, endereco, cpf);
        usuarioService.salvar(usuario);
    }

    public int login(String email, String senha) {
        return usuarioService.login(email, senha);
    }

    public int criarEmpresa(String tipoEmpresa, int idDono, String nome, String endereco, String tipoCozinha) throws ObjetoNaoEncontradoException, UsuarioSemPermissaoException, AtributoInvalidoException, EmpresaInvalidaException {
        Usuario usuario = usuarioService.buscar(idDono);

        if(usuario.getPermissoes().stream().noneMatch(p -> p.equals(Permissoes.CRIAR_EMRPESA)))
            throw new UsuarioSemPermissaoException("Usuario nao pode criar uma empresa");

        Empresa novaEmpresa = new Empresa(tipoEmpresa, (Dono) usuario, nome, endereco, tipoCozinha);
        return empresaService.salvar(novaEmpresa);
    }

    public String getEmpresasDoUsuario(int idDono) throws ObjetoNaoEncontradoException, UsuarioSemPermissaoException {
        Usuario usuario = usuarioService.buscar(idDono);

        if (usuario.getPermissoes().stream().noneMatch(p -> p.equals(Permissoes.CRIAR_EMRPESA)))
            throw new UsuarioSemPermissaoException("Usuario nao pode criar uma empresa");


        List<Empresa> empresas = empresaService.buscarEmpresasPorDono((Dono) usuario);

        List<List<String>> empresasString = empresas.stream()
                .map(e -> List.of(e.getNome(), e.getEndereco()))
                .toList();

        return "{" + empresasString + "}";
    }

    public int getIdEmpresa(int idDono, String nome, int indice) throws AtributoInvalidoException, ObjetoNaoEncontradoException, UsuarioSemPermissaoException {
        Usuario dono = usuarioService.buscar(idDono);

        if (dono.getPermissoes().stream().noneMatch(p -> p.equals(Permissoes.CRIAR_EMRPESA)))
            throw new UsuarioSemPermissaoException("Usuario nao pode criar uma empresa");

        Empresa empresa = empresaService.buscarIdEmpresa((Dono) dono, nome, indice);

        return empresa.getId();
    }

    public Empresa getEmpresa(int idEmpresa) throws ObjetoNaoEncontradoException {
        Empresa empresa = empresaService.buscarEmpresaPorId(idEmpresa);
        if(empresa == null)
            throw new ObjetoNaoEncontradoException("Empresa nao cadastrada");
        return empresa;
    }

    public int criarProduto(int idEmpresa, String nome, float valor, String categoria) throws AtributoInvalidoException, ObjetoNaoEncontradoException, ProdutoDuplicadoException {
        Empresa empresa = empresaService.buscarEmpresaPorId(idEmpresa);
        Produto produto = new Produto(nome, valor, categoria, empresa);

        produtoService.salvar(produto);
        empresa.adicionarProduto(produto);

        return produto.getId();
    }

    public void editarProduto(int idProduto, String nome, float valor, String categoria) throws ObjetoNaoEncontradoException, AtributoInvalidoException {
        Produto tempProduto = new Produto(nome, valor, categoria, null);
        produtoService.editarProduto(idProduto, tempProduto);
    }

    public String getProduto(String nome, int empresaId, String atributo) throws AtributoInvalidoException, ObjetoNaoEncontradoException {
        Empresa empresa = empresaService.buscarEmpresaPorId(empresaId);
        Produto produto = empresa.getProdutoPeloNome(nome);

        return produto.getAtributo(atributo);
    }


    public String listarProdutos(int empresaId) throws ObjetoNaoEncontradoException {
        Empresa empresa = empresaService.buscarEmpresaPorId(empresaId);

        if(empresa == null)
            throw new ObjetoNaoEncontradoException("Empresa nao encontrada");

        List<Produto> produtos = empresa.getProdutos();

        List<String> produtosString = produtos.stream()
                .map(Produto::getNome)
                .toList();

        return "{" + produtosString + "}";
    }

    public int criarPedido(int clienteId, int empresaId) throws ObjetoNaoEncontradoException, UsuarioSemPermissaoException, PedidoDuplicadoException {
        Usuario usuario = usuarioService.buscar(clienteId);

        if(usuario.getPermissoes().stream().noneMatch(p -> p.equals(Permissoes.FAZER_PEDIDO)))
            throw new UsuarioSemPermissaoException("Dono de empresa nao pode fazer um pedido");

        Empresa empresa = empresaService.buscarEmpresaPorId(empresaId);

        return pedidoService.criarPedido((Cliente) usuario, empresa);
    }

    public void adicionarProduto(int numero, int produto) throws ObjetoNaoEncontradoException, EstadoPedidoInvalidoException {
        if(pedidoService.buscarPedido(numero) == null)
            throw new ObjetoNaoEncontradoException("Nao existe pedido em aberto");
        Produto produtoObj = produtoService.buscar(produto);
        pedidoService.adicionarProduto(numero, produtoObj);
    }

    public String getPedido(int numero, String atributo) throws ObjetoNaoEncontradoException, AtributoInvalidoException {
        Pedido pedido = pedidoService.buscarPedido(numero);
        return pedido.getAtributo(atributo);
    }

    public void fecharPedido(int numero) throws ObjetoNaoEncontradoException {
        pedidoService.fecharPedido(numero);
    }

    public void removerProduto(int pedido, String produto) throws ObjetoNaoEncontradoException, AtributoInvalidoException, EstadoPedidoInvalidoException {
        pedidoService.removerProduto(pedido, produto);
    }

    public int getNumeroPedido(int cliente, int empresa, int indice) throws ObjetoNaoEncontradoException, AtributoInvalidoException {
        Cliente clienteObj = (Cliente) usuarioService.buscar(cliente);
        Empresa empresaObj = empresaService.buscarEmpresaPorId(empresa);
        return pedidoService.getNumeroPedido(clienteObj, empresaObj, indice);
    }
}
