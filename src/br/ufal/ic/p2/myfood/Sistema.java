package br.ufal.ic.p2.myfood;

import br.ufal.ic.p2.myfood.exceptions.*;
import br.ufal.ic.p2.myfood.models.*;
import br.ufal.ic.p2.myfood.services.EmpresaService;
import br.ufal.ic.p2.myfood.services.PedidoService;
import br.ufal.ic.p2.myfood.services.ProdutoService;
import br.ufal.ic.p2.myfood.services.UsuarioService;

import java.util.List;

/**
 * Classe respons�vel pelo gerenciamento do sistema de alimentos
 * Fornece m�todos para criar, atualizar, buscar e deletar usu�rios, empresas, produtos e pedidos
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
     * Obt�m a inst�ncia �nica do sistema
     *
     * @return A inst�ncia �nica do sistema
     */
    public static Sistema getInstance() {
        if (instance == null) {
            instance = new Sistema();
        }

        return instance;
    }

    /**
     * Zera o sistema, removendo todos os dados de empresas, usu�rios, produtos e pedidos
     */
    public void zerarSistema() {
        empresaService.deletarTodos();
        usuarioService.deletarTodos();
        produtoService.deletarTodos();
        pedidoService.deletarTodos();
    }

    /**
     * Encerra o sistema, salvando todos os dados de empresas, usu�rios, produtos e pedidos
     */
    public void encerrarSistema() {
        empresaService.salvarTodos();
        usuarioService.salvarTodos();
        produtoService.salvarTodos();
        pedidoService.salvarTodos();
    }

    /**
     * Obt�m o valor de um atributo de um usu�rio
     *
     * @param id       O identificador do usu�rio
     * @param atributo O nome do atributo
     * @return O valor do atributo
     * @throws AtributoInvalidoException    Se o atributo for inv�lido
     * @throws ObjetoNaoEncontradoException Se o usu�rio n�o for encontrado
     */
    public String getAtributoUsuario(int id, String atributo) throws AtributoInvalidoException, ObjetoNaoEncontradoException {
        Usuario usuario = usuarioService.buscar(id);
        return usuario.getAtributo(atributo);
    }

    /**
     * Cria um usu�rio do tipo Cliente
     *
     * @param nome     O nome do usu�rio
     * @param email    O e-mail do usu�rio
     * @param senha    A senha do usu�rio
     * @param endereco O endere�o do usu�rio
     * @throws AtributoInvalidoException Se algum atributo for inv�lido
     * @throws EmailExistenteException   Se o e-mail j� estiver em uso
     */
    public void criarUsuario(String nome, String email, String senha, String endereco) throws AtributoInvalidoException, EmailExistenteException {
        Usuario usuario = new Cliente(nome, email, senha, endereco);
        usuarioService.salvar(usuario);
    }

    /**
     * Cria um usu�rio do tipo Dono
     *
     * @param nome     O nome do usu�rio
     * @param email    O e-mail do usu�rio
     * @param senha    A senha do usu�rio
     * @param endereco O endere�o do usu�rio
     * @param cpf      O CPF do usu�rio
     * @throws AtributoInvalidoException Se algum atributo for inv�lido
     * @throws EmailExistenteException   Se o e-mail j� estiver em uso
     */
    public void criarUsuario(String nome, String email, String senha, String endereco, String cpf) throws AtributoInvalidoException, EmailExistenteException {
        Usuario usuario = new Dono(nome, email, senha, endereco, cpf);
        usuarioService.salvar(usuario);
    }

    /**
     * Realiza o login de um usu�rio
     *
     * @param email O e-mail do usu�rio
     * @param senha A senha do usu�rio
     * @return O ID do usu�rio autenticado
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
     * @param endereco    O endere�o da empresa
     * @param tipoCozinha O tipo de cozinha da empresa
     * @return O ID da nova empresa criada
     * @throws ObjetoNaoEncontradoException Se o dono n�o for encontrado
     * @throws UsuarioSemPermissaoException Se o dono n�o tiver permiss�o para criar uma empresa
     * @throws AtributoInvalidoException    Se algum atributo for inv�lido
     * @throws EmpresaInvalidaException     Se a empresa for inv�lida
     */
    public int criarEmpresa(String tipoEmpresa, int idDono, String nome, String endereco, String tipoCozinha) throws ObjetoNaoEncontradoException, UsuarioSemPermissaoException, AtributoInvalidoException, EmpresaInvalidaException {
        Usuario usuario = usuarioService.buscar(idDono);

        if (usuario.getPermissoes().stream().noneMatch(p -> p.equals(Permissoes.CRIAR_EMRPESA)))
            throw new UsuarioSemPermissaoException("Usuario nao pode criar uma empresa");

        Empresa novaEmpresa = new Empresa(tipoEmpresa, (Dono) usuario, nome, endereco, tipoCozinha);
        return empresaService.salvar(novaEmpresa);
    }

    /**
     * Obt�m as empresas associadas a um dono
     *
     * @param idDono O ID do dono
     * @return Uma string com as informa��es das empresas do dono
     * @throws ObjetoNaoEncontradoException Se o dono n�o for encontrado
     * @throws UsuarioSemPermissaoException Se o dono n�o tiver permiss�o para criar uma empresa
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
     * Obt�m o ID de uma empresa associada a um dono com base no nome e �ndice
     *
     * @param idDono O ID do dono
     * @param nome   O nome da empresa
     * @param indice O �ndice da empresa
     * @return O ID da empresa
     * @throws AtributoInvalidoException    Se algum atributo for inv�lido
     * @throws ObjetoNaoEncontradoException Se o dono n�o for encontrado ou a empresa n�o existir
     * @throws UsuarioSemPermissaoException Se o dono n�o tiver permiss�o para criar uma empresa
     */
    public int getIdEmpresa(int idDono, String nome, int indice) throws AtributoInvalidoException, ObjetoNaoEncontradoException, UsuarioSemPermissaoException {
        Usuario dono = usuarioService.buscar(idDono);

        if (dono.getPermissoes().stream().noneMatch(p -> p.equals(Permissoes.CRIAR_EMRPESA)))
            throw new UsuarioSemPermissaoException("Usuario nao pode criar uma empresa");

        Empresa empresa = empresaService.buscarIdEmpresa((Dono) dono, nome, indice);

        return empresa.getId();
    }

    /**
     * Obt�m uma empresa pelo ID
     *
     * @param idEmpresa O ID da empresa
     * @return A empresa correspondente ao ID
     * @throws ObjetoNaoEncontradoException Se a empresa n�o for encontrada
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
     * @throws AtributoInvalidoException    Se algum atributo for inv�lido
     * @throws ObjetoNaoEncontradoException Se a empresa n�o for encontrada
     * @throws ProdutoDuplicadoException    Se o produto j� existir
     */
    public int criarProduto(int idEmpresa, String nome, float valor, String categoria) throws AtributoInvalidoException, ObjetoNaoEncontradoException, ProdutoDuplicadoException {
        Empresa empresa = empresaService.buscarEmpresaPorId(idEmpresa);
        Produto produto = new Produto(nome, valor, categoria, empresa);

        produtoService.salvar(produto);
        empresa.adicionarProduto(produto);

        return produto.getId();
    }

    /**
     * Edita as informa��es de um produto
     *
     * @param idProduto O ID do produto
     * @param nome      O novo nome do produto
     * @param valor     O novo valor do produto
     * @param categoria A nova categoria do produto
     * @throws ObjetoNaoEncontradoException Se o produto n�o for encontrado
     * @throws AtributoInvalidoException    Se algum atributo for inv�lido
     */
    public void editarProduto(int idProduto, String nome, float valor, String categoria) throws ObjetoNaoEncontradoException, AtributoInvalidoException {
        Produto tempProduto = new Produto(nome, valor, categoria, null);
        produtoService.editarProduto(idProduto, tempProduto);
    }

    /**
     * Obt�m o valor de um atributo de um produto
     *
     * @param nome      O nome do produto
     * @param empresaId O ID da empresa
     * @param atributo  O nome do atributo
     * @return O valor do atributo
     * @throws AtributoInvalidoException    Se o atributo for inv�lido
     * @throws ObjetoNaoEncontradoException Se a empresa ou o produto n�o forem encontrados
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
     * @throws ObjetoNaoEncontradoException Se a empresa n�o for encontrada
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
     * @throws ObjetoNaoEncontradoException Se o cliente ou a empresa n�o forem encontrados
     * @throws UsuarioSemPermissaoException Se o cliente n�o tiver permiss�o para fazer um pedido
     * @throws PedidoDuplicadoException     Se o pedido j� existir
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
     * @param numero  O n�mero do pedido
     * @param produto O ID do produto
     * @throws ObjetoNaoEncontradoException  Se o pedido ou o produto n�o forem encontrados
     * @throws EstadoPedidoInvalidoException Se o pedido n�o estiver em um estado v�lido para adi��o de produtos
     */
    public void adicionarProduto(int numero, int produto) throws ObjetoNaoEncontradoException, EstadoPedidoInvalidoException {
        if (pedidoService.buscarPedido(numero) == null)
            throw new ObjetoNaoEncontradoException("Nao existe pedido em aberto");
        Produto produtoObj = produtoService.buscar(produto);
        pedidoService.adicionarProduto(numero, produtoObj);
    }

    /**
     * Obt�m o valor de um atributo de um pedido
     *
     * @param numero   O n�mero do pedido
     * @param atributo O nome do atributo
     * @return O valor do atributo
     * @throws ObjetoNaoEncontradoException Se o pedido n�o for encontrado
     * @throws AtributoInvalidoException    Se o atributo for inv�lido
     */
    public String getPedido(int numero, String atributo) throws ObjetoNaoEncontradoException, AtributoInvalidoException {
        Pedido pedido = pedidoService.buscarPedido(numero);
        return pedido.getAtributo(atributo);
    }

    /**
     * Fecha um pedido
     *
     * @param numero O n�mero do pedido
     * @throws ObjetoNaoEncontradoException Se o pedido n�o for encontrado
     */
    public void fecharPedido(int numero) throws ObjetoNaoEncontradoException {
        pedidoService.fecharPedido(numero);
    }

    /**
     * Remove um produto de um pedido
     *
     * @param pedido  O n�mero do pedido
     * @param produto O nome do produto
     * @throws ObjetoNaoEncontradoException  Se o pedido ou o produto n�o forem encontrados
     * @throws AtributoInvalidoException     Se o atributo for inv�lido
     * @throws EstadoPedidoInvalidoException Se o pedido n�o estiver em um estado v�lido para remo��o de produtos
     */
    public void removerProduto(int pedido, String produto) throws ObjetoNaoEncontradoException, AtributoInvalidoException, EstadoPedidoInvalidoException {
        pedidoService.removerProduto(pedido, produto);
    }

    /**
     * Obt�m o n�mero de um pedido com base no cliente, empresa e �ndice
     *
     * @param cliente O ID do cliente
     * @param empresa O ID da empresa
     * @param indice  O �ndice do pedido
     * @return O n�mero do pedido
     * @throws ObjetoNaoEncontradoException Se o cliente, a empresa ou o pedido n�o forem encontrados
     * @throws AtributoInvalidoException    Se o atributo for inv�lido
     */
    public int getNumeroPedido(int cliente, int empresa, int indice) throws ObjetoNaoEncontradoException, AtributoInvalidoException {
        Cliente clienteObj = (Cliente) usuarioService.buscar(cliente);
        Empresa empresaObj = empresaService.buscarEmpresaPorId(empresa);
        return pedidoService.getNumeroPedido(clienteObj, empresaObj, indice);
    }
}
