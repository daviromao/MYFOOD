# Projeto da Disciplina de P2 - MyFood

## Relatório de Entrega 1 e 2

Alunos: "Davi da Silva Romão"

## Introdução:

O sistema desenvolvido foi o MyFood, um sistema de gestão de pedidos de um restaurante.

No sistema é possível cadastrar empresas, usuarios, pedidos e produtos.
O projeto foi desenvolvido em Java, com o uso da prática de programação TDD: Test Driven Development. Como forma de persistência e simulação de banco de dados, foi utilizado um HashMap e XML para armazenar os dados.

## Principais Componentes:

#### Models:

Classes que estrutura os dados do sistema.

- **Empresa**: Representa uma empresa abstrata que pode ter vários produtos e pedidos.
- **Produto**: Representa um produto que pode ser vendido por uma empresa.
- **Pedido**: Representa um pedido que pode ter vários produtos.
- **Entrega**: Representa a entrega e um pedido pronto para ser entregue.
- **Veiculo**: Representa um veiculo de um entregador, pensado na possibilidade futura de ter mais veículos para um entregador.
- **Usuario**: Representa um usuário abstrato que pode ser um cliente, dono de empresa ou entregador.
- **Permissão**: Enum que representa as permissões de um usuário, e. g., criar empresa, entregar pedido.

#### Persistencia:

Interface que define os métodos de persistência. Como implementação, foi desenvolvida a classe PersistenciaXML que persiste os objetos em um arquivo XML.
A classe PersistenciaXML e a própria interface foi criada utilizando generics, o que permite que a classe seja genérica e possa ser utilizada para persistir qualquer objeto.

#### Serviços:

Implementa as regras de negócio de cada modelo. Ao total são os seguintes serviços: EmpresaService, PedidoService, ProdutoService, UsuarioService (Dono, Cliente e Entregador) e EntregaService.

#### Sistema

Classe responsável pro fazer a orquestração das ações do sistema em um nível mais alto. Relaciona diversos serviços para realizar ações mais complexas.

#### Facade

A facade é a classe utilizada apenas como porta de entrada para o EasyAccept. Ela é responsável por receber os comandos do EasyAccept e chamar os métodos corretos do sistema.

## Padrões de Projeto:

### Facade

- **Descrição Geral**: Fornecer uma interface unificada para um conjunto de interfaces em um subsistema. Facade define uma interface de nível mais alto que torna o subsistema mais fácil de usar.
- **Problema Resolvido**: Interação com o sistema de forma mais simples e unificada.
- **Identificação da Oportunidade**: A necessidade de criar uma classe que orquestrasse as ações do sistema em um nível mais alto.
- **Aplicação do Padrão**: A classe Facade foi criada para receber os comandos do EasyAccept e chamar os métodos corretos do sistema.

### Singleton

- **Descrição Geral**: Garantir que uma classe tenha apenas uma instância e fornecer um ponto de acesso global para a instância.
- **Problema Resolvido**: Garantir que uma classe tenha apenas uma instância.
- **Identificação da Oportunidade**: A necessidade de garantir que a classe de sistema tenha apenas uma instância.
- **Aplicação do Padrão**: A classe Sistema foi implementada como um Singleton.

```java
    private static Sistema instance;
    ...
    public static Sistema getInstance() {
        if (instance == null) {
            instance = new Sistema();
        }
        return instance;
    }
```

## Decisões:

Muitas das decisões tomada na criação do projeto tem como principal objetivo pensar em problemas comuns já conhecidos na área de desenvolvimento de software, como por exemplo, a manutenção do código, a escalabilidade do projeto e a facilidade de implementação de novas funcionalidades. Em outras palavras, melhorar a coesão, diminuir o acoplamento do código, ocultar informações para evitar que o código seja dependente de detalhes de implementação e a reutilização de código.

Uma das principais decisões foi a criação de uma interface "Persistencia" que consegue gerenciar a persistência dos objetos do sistema. Dessa forma, fazemos nosso sistema depender de uma interface e não de uma implementação, o que facilita a troca de implementação de persistência, caso seja necessário.

Outra decisão, foi dividir as responsabilidades em camadas e permitir que cada camda apenas execute o que é de sua responsabilidade. Por exemplo, a camada de persistência é responsável por persistir os objetos, a camada de negócio é responsável por implementar as regras de negócio e a camada de apresentação (que pode ser considerado a Facade ou Sistema, há depende de sua interpretação) é responsável por interagir com o usuário.

Nesse sentido, as regras de negócio são implementadas especificamente nos serviços. Exemplo:

```java
    public int salvar(Empresa novaEmpresa) throws EmpresaInvalidaException {
        for (Empresa e : empresas.buscarTodos()) {
            if(e.getNome().equals(novaEmpresa.getNome())){
                if(!e.getDono().equals(novaEmpresa.getDono())) {
                    throw new EmpresaInvalidaException("Empresa com esse nome ja existe");
                }
                if(e.getDono().equals(novaEmpresa.getDono()) && e.getEndereco().equals(novaEmpresa.getEndereco())) {
                    throw new EmpresaInvalidaException("Proibido cadastrar duas empresas com o mesmo nome e local");
                }
            }
        }

        empresas.salvar(novaEmpresa);

        return novaEmpresa.getId();
    }
```

Nesse exemplo, dois donos não podem ter empresas com o mesmo nome e um dono não pode ter duas empresas com o mesmo nome e endereço. Essa regra de negócio é implementada no serviço de empresa.

Isso facilita com que as regras de negócio sejam facilmente alteradas e testadas, pois estão isoladas em uma classe específica, além de permitir identificar facilmente algum bug na regra de negócio.

Outro detalhe muito importante é a própria utilização da Orientação a Objeto para nos ajudar a lidar com abstrações e polimorfismo. Por exemplo, usuário é uma classe abstrata que possui dois filhos: Cliente, Dono e Restaurante. Ambas as classes filhas possuem muitas informações em comum, mas também possuem informações específicas. Dessa forma, podemos tratar um Cliente e um Dono como um Usuário, o que facilita a implementação de métodos que lidam com Usuários, sem a necessidade de saber se é um Cliente ou um Dono.

Adiante, ainda nesse sentido, ao invés de deixar as classes apenas como uma "entidade" que possui apenas atributos, foi implementado métodos que fazem sentido para a classe. Por exemplo, a classe Pedido possui um método "adicionarProduto" que adiciona um produto ao pedido. Isso faz sentido, pois um pedido pode ter produtos. Dessa forma, a classe Pedido não é apenas uma "entidade" que possui atributos, mas também possui comportamentos. Além do pedido poder calcular o valor total do pedido, adicionar produtos, remover produtos, etc.

Então, comportamentos intrínsecos a uma classe foram implementados na própria classe e comportamentos que envolvem mais de uma classe e regras de NEGÓCIO foram implementados na camada de negócio. Uma vez que uma empresa não deveria saber como salvar ou se existe outra empresa com o mesmo nome.

Como dito em aula, quando há um lançamento de exceção ao criar um objeto, o desenvolvedor vai direto na classe que deu problema, por isso, segui as recomendações de adicionar a validação no construtor.

## Diagrama de Classes
 Foi utilizado a versão UML apresentada no livro "Engenharia de Software Moderna".

O objetivo deste diagrama é apenas dar uma visão geral do sistema, desconsiderando métodos como getters e setters, métodos de validação e também regras de negócio. A modelagem é apenas um versão simplificada de como as classes do projeto se relacionam entre si. Assim, como exemplo, podemos observar que um produto pertence a uma empresa, que pode fazer parte de um pedido e que um entregador é responsável pela entrega do pedido para um cliente.

![image](https://github.com/user-attachments/assets/ada9f2c9-443e-4e49-9f62-4698a747ae96)

Referencia: Marco Tulio Valente. Engenharia de Software Moderna: Princípios e Práticas para Desenvolvimento de Software com Produtividade, Editora: Independente, 2020.


## Conclusão:

Foi possível implementar um sistema de gestão de pedidos de um restaurante utilizando a prática de TDD e a divisão de responsabilidades em camadas. O sistema é capaz de cadastrar empresas, usuários, produtos e pedidos. Além disso, foi possível ver algumas oportunidades de aprendizado e refatoramento de código durante a criação.

A entrega para a Milestone 2 não obteve problemas significativos, haja visdta que o projeto já havia sido criado inicialmente com o objetivo de ser fácil a criação de novas features. Assim, não foi necessário refatorar grandes linhas de código para que fosse possível a adição de novos tipos de empresas, usuários e feature de entrega.
