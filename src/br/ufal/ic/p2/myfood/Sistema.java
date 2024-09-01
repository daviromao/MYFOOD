package br.ufal.ic.p2.myfood;

import br.ufal.ic.p2.myfood.exceptions.*;
import br.ufal.ic.p2.myfood.models.*;
import br.ufal.ic.p2.myfood.services.EmpresaService;
import br.ufal.ic.p2.myfood.services.PedidoService;
import br.ufal.ic.p2.myfood.services.ProdutoService;
import br.ufal.ic.p2.myfood.services.UsuarioService;

import java.util.List;

/**
 * Classe responsável pelo gerenciamento do sistema de alimentos
 * Fornece métodos para criar, atualizar, buscar e deletar usuários, empresas, produtos e pedidos
 */
public class Sistema {
    private static Sistema instance;

    private UsuarioService usuarioService = new UsuarioService();
    private EmpresaService empresaService = new EmpresaService();
    private ProdutoService produtoService = new ProdutoService();
    private PedidoService pedidoService = new PedidoService();

    private Sistema() {
    }

    /**
     * Obtém a instância única do sistema
     *
     * @return A instância única do sistema
     */
    public static Sistema getInstance() {
        if (instance == null) {
            instance = new Sistema();
        }

        return instance;
    }

    /**
     * Zera o sistema, removendo todos os dados de empresas, usuários, produtos e pedidos
     */
    public void zerarSistema() {
        empresaService.deletarTodos();
        usuarioService.deletarTodos();
        produtoService.deletarTodos();
        pedidoService.deletarTodos();
    }

    /**
     * Encerra o sistema, salvando todos os dados de empresas, usuários, produtos e pedidos
     */
    public void encerrarSistema() {
        empresaService.salvarTodos();
        usuarioService.salvarTodos();
        produtoService.salvarTodos();
        pedidoService.salvarTodos();
    }

    /**
     * Obtém o valor de um atributo de um usuário
     *
     * @param id       O identificador do usuário
     * @param atributo O nome do atributo
     * @return O valor do atributo
     * @throws AtributoInvalidoException    Se o atributo for inválido
     * @throws ObjetoNaoEncontradoException Se o usuário não for encontrado
     */
    public String getAtributoUsuario(int id, String atributo) throws AtributoInvalidoException, ObjetoNaoEncontradoException {
        Usuario usuario = usuarioService.buscar(id);
        return usuario.getAtributo(atributo);
    }

    /**
     * Cria um usuário do tipo Cliente
     *
     * @param nome     O nome do usuário
     * @param email    O e-mail do usuário
     * @param senha    A senha do usuário
     * @param endereco O endereço do usuário
     * @throws AtributoInvalidoException Se algum atributo for inválido
     * @throws EmailExistenteException   Se o e-mail já estiver em uso
     */
    public void criarUsuario(String nome, String email, String senha, String endereco) throws AtributoInvalidoException, EmailExistenteException {
        Usuario usuario = new Cliente(nome, email, senha, endereco);
        usuarioService.salvar(usuario);
    }

    /**
     * Cria um usuário do tipo Dono
     *
     * @param nome     O nome do usuário
     * @param email    O e-mail do usuário
     * @param senha    A senha do usuário
     * @param endereco O endereço do usuário
     * @param cpf      O CPF do usuário
     * @throws AtributoInvalidoException Se algum atributo for inválido
     * @throws EmailExistenteException   Se o e-mail já estiver em uso
     */
    public void criarUsuario(String nome, String email, String senha, String endereco, String cpf) throws AtributoInvalidoException, EmailExistenteException {
        Usuario usuario = new Dono(nome, email, senha, endereco, cpf);
        usuarioService.salvar(usuario);
    }

    /**
     * Realiza o login de um usuário
     *
     * @param email O e-mail do usuário
     * @param senha A senha do usuário
     * @return O ID do usuário autenticado
     */
    public int login(String email, String senha) {
        return usuarioService.login(email, senha);
    }

    /**
     * Cria uma empresa associada a um dono
     *
     * @param tipoEmpresa O tipo da empresa
     * @param idDono      O ID do dono da empresa
     * @param nome        O nome da empresa
     * @param endereco    O endereço da empresa
     * @param tipoCozinha O tipo de cozinha da empresa
     * @return O ID da nova empresa criada
     * @throws ObjetoNaoEncontradoException Se o dono não for encontrado
     * @throws UsuarioSemPermissaoException Se o dono não tiver permissão para criar uma empresa
     * @throws AtributoInvalidoException    Se algum atributo for inválido
     * @throws EmpresaInvalidaException     Se a empresa for inválida
     */
    public int criarEmpresa(String tipoEmpresa, int idDono, String nome, String endereco, String tipoCozinha) throws ObjetoNaoEncontradoException, UsuarioSemPermissaoException, AtributoInvalidoException, EmpresaInvalidaException {
        Usuario usuario = usuarioService.buscar(idDono);

        if (usuario.getPermissoes().stream().noneMatch(p -> p.equals(Permissoes.CRIAR_EMRPESA)))
            throw new UsuarioSemPermissaoException("Usuario nao pode criar uma empresa");

        Empresa novaEmpresa = new Empresa(tipoEmpresa, (Dono) usuario, nome, endereco, tipoCozinha);
        return empresaService.salvar(novaEmpresa);
    }

    /**
     * Obtém as empresas associadas a um dono
     *
     * @param idDono O ID do dono
     * @return Uma string com as informações das empresas do dono
     * @throws ObjetoNaoEncontradoException Se o dono não for encontrado
     * @throws UsuarioSemPermissaoException Se o dono não tiver permissão para criar uma empresa
     */
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

    /**
     * Obtém o ID de uma empresa associada a um dono com base no nome e índice
     *
     * @param idDono O ID do dono
     * @param nome   O nome da empresa
     * @param indice O índice da empresa
     * @return O ID da empresa
     * @throws AtributoInvalidoException    Se algum atributo for inválido
     * @throws ObjetoNaoEncontradoException Se o dono não for encontrado ou a empresa não existir
     * @throws UsuarioSemPermissaoException Se o dono não tiver permissão para criar uma empresa
     */
    public int getIdEmpresa(int idDono, String nome, int indice) throws AtributoInvalidoException, ObjetoNaoEncontradoException, UsuarioSemPermissaoException {
        Usuario dono = usuarioService.buscar(idDono);

        if (dono.getPermissoes().stream().noneMatch(p -> p.equals(Permissoes.CRIAR_EMRPESA)))
            throw new UsuarioSemPermissaoException("Usuario nao pode criar uma empresa");

        Empresa empresa = empresaService.buscarIdEmpresa((Dono) dono, nome, indice);

        return empresa.getId();
    }

    /**
     * Obtém uma empresa pelo ID
     *
     * @param idEmpresa O ID da empresa
     * @return A empresa correspondente ao ID
     * @throws ObjetoNaoEncontradoException Se a empresa não for encontrada
     */
    public Empresa getEmpresa(int idEmpresa) throws ObjetoNaoEncontradoException {
        Empresa empresa = empresaService.buscarEmpresaPorId(idEmpresa);
        if (empresa == null)
            throw new ObjetoNaoEncontradoException("Empresa nao cadastrada");
        return empresa;
    }

    /**
     * Cria um produto e o associa a uma empresa
     *
     * @param idEmpresa O ID da empresa
     * @param nome      O nome do produto
     * @param valor     O valor do produto
     * @param categoria A categoria do produto
     * @return O ID do novo produto
     * @throws AtributoInvalidoException    Se algum atributo for inválido
     * @throws ObjetoNaoEncontradoException Se a empresa não for encontrada
     * @throws ProdutoDuplicadoException    Se o produto já existir
     */
    public int criarProduto(int idEmpresa, String nome, float valor, String categoria) throws AtributoInvalidoException, ObjetoNaoEncontradoException, ProdutoDuplicadoException {
        Empresa empresa = empresaService.buscarEmpresaPorId(idEmpresa);
        Produto produto = new Produto(nome, valor, categoria, empresa);

        produtoService.salvar(produto);
        empresa.adicionarProduto(produto);

        return produto.getId();
    }

    /**
     * Edita as informações de um produto
     *
     * @param idProduto O ID do produto
     * @param nome      O novo nome do produto
     * @param valor     O novo valor do produto
     * @param categoria A nova categoria do produto
     * @throws ObjetoNaoEncontradoException Se o produto não for encontrado
     * @throws AtributoInvalidoException    Se algum atributo for inválido
     */
    public void editarProduto(int idProduto, String nome, float valor, String categoria) throws ObjetoNaoEncontradoException, AtributoInvalidoException {
        Produto tempProduto = new Produto(nome, valor, categoria, null);
        produtoService.editarProduto(idProduto, tempProduto);
    }

    /**
     * Obtém o valor de um atributo de um produto
     *
     * @param nome      O nome do produto
     * @param empresaId O ID da empresa
     * @param atributo  O nome do atributo
     * @return O valor do atributo
     * @throws AtributoInvalidoException    Se o atributo for inválido
     * @throws ObjetoNaoEncontradoException Se a empresa ou o produto não forem encontrados
     */
    public String getProduto(String nome, int empresaId, String atributo) throws AtributoInvalidoException, ObjetoNaoEncontradoException {
        Empresa empresa = empresaService.buscarEmpresaPorId(empresaId);
        Produto produto = empresa.getProdutoPeloNome(nome);

        return produto.getAtributo(atributo);
    }

    /**
     * Lista os nomes dos produtos de uma empresa
     *
     * @param empresaId O ID da empresa
     * @return Uma string com os nomes dos produtos
     * @throws ObjetoNaoEncontradoException Se a empresa não for encontrada
     */
    public String listarProdutos(int empresaId) throws ObjetoNaoEncontradoException {
        Empresa empresa = empresaService.buscarEmpresaPorId(empresaId);

        if (empresa == null)
            throw new ObjetoNaoEncontradoException("Empresa nao encontrada");

        List<Produto> produtos = empresa.getProdutos();

        List<String> produtosString = produtos.stream()
                .map(Produto::getNome)
                .toList();

        return "{" + produtosString + "}";
    }

    /**
     * Cria um pedido para um cliente em uma empresa
     *
     * @param clienteId O ID do cliente
     * @param empresaId O ID da empresa
     * @return O ID do novo pedido
     * @throws ObjetoNaoEncontradoException Se o cliente ou a empresa não forem encontrados
     * @throws UsuarioSemPermissaoException Se o cliente não tiver permissão para fazer um pedido
     * @throws PedidoDuplicadoException     Se o pedido já existir
     */
    public int criarPedido(int clienteId, int empresaId) throws ObjetoNaoEncontradoException, UsuarioSemPermissaoException, PedidoDuplicadoException {
        Usuario usuario = usuarioService.buscar(clienteId);

        if (usuario.getPermissoes().stream().noneMatch(p -> p.equals(Permissoes.FAZER_PEDIDO)))
            throw new UsuarioSemPermissaoException("Dono de empresa nao pode fazer um pedido");

        Empresa empresa = empresaService.buscarEmpresaPorId(empresaId);

        return pedidoService.criarPedido((Cliente) usuario, empresa);
    }

    /**
     * Adiciona um produto a um pedido
     *
     * @param numero  O número do pedido
     * @param produto O ID do produto
     * @throws ObjetoNaoEncontradoException  Se o pedido ou o produto não forem encontrados
     * @throws EstadoPedidoInvalidoException Se o pedido não estiver em um estado válido para adição de produtos
     */
    public void adicionarProduto(int numero, int produto) throws ObjetoNaoEncontradoException, EstadoPedidoInvalidoException {
        if (pedidoService.buscarPedido(numero) == null)
            throw new ObjetoNaoEncontradoException("Nao existe pedido em aberto");
        Produto produtoObj = produtoService.buscar(produto);
        pedidoService.adicionarProduto(numero, produtoObj);
    }

    /**
     * Obtém o valor de um atributo de um pedido
     *
     * @param numero   O número do pedido
     * @param atributo O nome do atributo
     * @return O valor do atributo
     * @throws ObjetoNaoEncontradoException Se o pedido não for encontrado
     * @throws AtributoInvalidoException    Se o atributo for inválido
     */
    public String getPedido(int numero, String atributo) throws ObjetoNaoEncontradoException, AtributoInvalidoException {
        Pedido pedido = pedidoService.buscarPedido(numero);
        return pedido.getAtributo(atributo);
    }

    /**
     * Fecha um pedido
     *
     * @param numero O número do pedido
     * @throws ObjetoNaoEncontradoException Se o pedido não for encontrado
     */
    public void fecharPedido(int numero) throws ObjetoNaoEncontradoException {
        pedidoService.fecharPedido(numero);
    }

    /**
     * Remove um produto de um pedido
     *
     * @param pedido  O número do pedido
     * @param produto O nome do produto
     * @throws ObjetoNaoEncontradoException  Se o pedido ou o produto não forem encontrados
     * @throws AtributoInvalidoException     Se o atributo for inválido
     * @throws EstadoPedidoInvalidoException Se o pedido não estiver em um estado válido para remoção de produtos
     */
    public void removerProduto(int pedido, String produto) throws ObjetoNaoEncontradoException, AtributoInvalidoException, EstadoPedidoInvalidoException {
        pedidoService.removerProduto(pedido, produto);
    }

    /**
     * Obtém o número de um pedido com base no cliente, empresa e índice
     *
     * @param cliente O ID do cliente
     * @param empresa O ID da empresa
     * @param indice  O índice do pedido
     * @return O número do pedido
     * @throws ObjetoNaoEncontradoException Se o cliente, a empresa ou o pedido não forem encontrados
     * @throws AtributoInvalidoException    Se o atributo for inválido
     */
    public int getNumeroPedido(int cliente, int empresa, int indice) throws ObjetoNaoEncontradoException, AtributoInvalidoException {
        Cliente clienteObj = (Cliente) usuarioService.buscar(cliente);
        Empresa empresaObj = empresaService.buscarEmpresaPorId(empresa);
        return pedidoService.getNumeroPedido(clienteObj, empresaObj, indice);
    }
}
