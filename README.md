# Projeto da Disciplina de P2 - MyFood

## Relatório de Entrega 1

Alunos: "Davi da Silva Romão"

## Introdução:

O sistema desenvolvido foi o MyFood, um sistema de gestão de pedidos de um restaurante.

No sistema é possível cadastrar empresas, usuarios, pedidos e produtos.
O projeto foi desenvolvido em Java, com o uso da prática de programação TDD: Test Driven Development. Como forma de persistência e simulação de banco de dados, foi utilizado um HashMap e XML para armazenar os dados.

## Principais Componentes:

#### Models:

Classes que estrutura os dados do sistema.

- **Empresa**: Representa uma empresa que pode ter vários produtos e pedidos.
- **Produto**: Representa um produto que pode ser vendido por uma empresa.
- **Pedido**: Representa um pedido que pode ter vários produtos.
- **Usuario**: Representa um usuário que pode ser um cliente ou um dono de empresa.
- **Permissão**: Enum que representa as permissões de um usuário, e. g., criar empresa.

#### Persistencia:

Interface que define os métodos de persistência. Como implementação, foi desenvolvida a classe PersistenciaXML que persiste os objetos em um arquivo XML.
A classe PersistenciaXML e a própria interface foi criada utilizando generics, o que permite que a classe seja genérica e possa ser utilizada para persistir qualquer objeto.

#### Serviços:

Implementa as regras de negócio de cada modelo.

#### Sistema

Classe responsável pro fazer a orquestração das ações do sistema em um nível mais alto. Relaciona diversos serviços para realizar ações mais complexas.

#### Facade

A facade é a classe utilizada apenas como porta de entrada para o EasyAccept. Ela é responsável por receber os comandos do EasyAccept e chamar os métodos corretos do sistema.

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

Outro detalhe muito importante é a própria utilização da Orientação a Objeto para nos ajudar a lidar com abstrações e polimorfismo. Por exemplo, usuário é uma classe abstrata que possui dois filhos: Cliente e Dono. Ambas as classes filhas possuem muitas informações em comum, mas também possuem informações específicas. Dessa forma, podemos tratar um Cliente e um Dono como um Usuário, o que facilita a implementação de métodos que lidam com Usuários, sem a necessidade de saber se é um Cliente ou um Dono.

Adiante, ainda nesse sentido, ao invés de deixar as classes apenas como uma "entidade" que possui apenas atributos, foi implementado métodos que fazem sentido para a classe. Por exemplo, a classe Pedido possui um método "adicionarProduto" que adiciona um produto ao pedido. Isso faz sentido, pois um pedido pode ter produtos. Dessa forma, a classe Pedido não é apenas uma "entidade" que possui atributos, mas também possui comportamentos. Além do pedido poder calcular o valor total do pedido, adicionar produtos, remover produtos, etc.

Então, comportamentos intrínsecos a uma classe foram implementados na própria classe e comportamentos que envolvem mais de uma classe e regras de NEGÓCIO foram implementados na camada de negócio. Uma vez que uma empresa não deveria saber como salvar ou se existe outra empresa com o mesmo nome.

Como dito em aula, quando há um lançamento de exceção ao criar um objeto, o desenvolvedor vai direto na classe que deu problema, por isso, segui as recomendações de adicionar a validação no construtor.

## Conclusão:

Foi possível implementar um sistema de gestão de pedidos de um restaurante utilizando a prática de TDD e a divisão de responsabilidades em camadas. O sistema é capaz de cadastrar empresas, usuários, produtos e pedidos. Além disso, foi possível ver algumas oportunidades de aprendizado e refatoramento de código durante a criação.
