Projeto Java CRUD com PostgreSQL
--------------------------
Este é um projeto simples em Java que demonstra operações básicas de CRUD (Create, Read, Update, Delete) em um banco de dados PostgreSQL. Ele foi desenvolvido como um exemplo prático de conexão e manipulação de dados em um ambiente de banco de dados real (usando Aiven como provedor de nuvem).

Sumário
--------------------------
1. Visão Geral

2. Funcionalidades

3. Pré-requisitos

4. Configuração do Banco de Dados

5. Configuração do Projeto

6. Como Executar

7. Estrutura da Tabela

8. Tecnologias Utilizadas

1. Visão Geral

Este projeto Java ilustra como estabelecer uma conexão JDBC com um banco de dados PostgreSQL (hospedado na Aiven, com conexão SSL obrigatória) e performar as seguintes operações:

Create (Criar): Inserir novos registros na tabela produtos.

Read (Ler): Listar todos os registros existentes na tabela produtos.

Update (Atualizar): Modificar dados de um registro existente na tabela produtos.

Delete (Deletar): Remover registros da tabela produtos.

2. Funcionalidades

Conexão segura com PostgreSQL usando SSL (sslmode=require).

Criação/Recriação automática da tabela public.produtos ao iniciar a aplicação (útil para desenvolvimento e testes).

Operações CRUD para a entidade Produto (ID, Nome, Preço, Quantidade).

3. Pré-requisitos

Para rodar este projeto, você precisará ter o seguinte instalado:

Java Development Kit (JDK) 8 ou superior.

Apache Maven (para gerenciamento de dependências, se estiver usando um projeto Maven).

Um servidor PostgreSQL acessível (neste projeto, utilizamos a Aiven).

pgAdmin (opcional, mas recomendado para gerenciar seu banco de dados e usuários).

4. Configuração do Banco de Dados

Crie um servidor PostgreSQL na Aiven (ou em seu ambiente local).

Crie um usuário (ex: teste) no seu banco de dados PostgreSQL usando o pgAdmin ou comandos SQL.

No pgAdmin, clique direito em "Login/Group Roles" > "Create" > "Login/Group Role...". Defina um nome de usuário e uma senha.

Conceda a permissão CONNECT ao seu usuário no banco de dados desejado (ex: defaultdb).

No pgAdmin, clique direito no seu banco de dados > "Properties..." > Aba "Privileges". Adicione seu usuário e marque CONNECT.

Conceda a permissão CREATE ao seu usuário no schema public. Isso é crucial para que a aplicação possa criar a tabela.

No pgAdmin (conectado como superusuário), execute:
GRANT CREATE ON SCHEMA public TO seu_usuario;
(Substitua seu_usuario pelo nome do seu usuário, ex: teste).

Obtenha as credenciais de conexão da Aiven: Host, Porta, Nome do Banco de Dados, Usuário e Senha.

5. Clone este repositório para sua máquina local.

Adicione a dependência do driver JDBC do PostgreSQL ao seu projeto. Se estiver usando Maven, adicione o seguinte ao seu pom.xml:
<dependencies>
    <dependency>
        <groupId>org.postgresql</groupId>
        <artifactId>postgresql</artifactId>
        <version>42.7.3</version> </dependency>
</dependencies>

Se não usar Maven, baixe o JAR do driver (encontrado em PostgreSQL JDBC Driver) e adicione-o ao classpath do seu projeto.

Atualize as informações de conexão na classe br.crud.GerenciadorCRUD.java:
// ... dentro de GerenciadorCRUD.java
String urlAiven = "jdbc:postgresql://<SEU_HOST_DA_AIVEN>:<SUA_PORTA_DA_AIVEN>/<SEU_NOME_DO_BD>?sslmode=require";
String usuarioAiven = "seu_usuario"; // Ex: teste
String senhaAiven = "sua_senha"; // Ex: teste123
Substitua os placeholders com as credenciais reais do seu servidor Aiven.

6. Como Executar

Compile o projeto (se estiver usando Maven, mvn clean install).

Execute a classe principal br.crud.GerenciadorCRUD.

Ao executar, a aplicação fará o seguinte:

Conectará ao banco de dados.

Deletará a tabela produtos (se existir) e a recriará.

Inserirá dois produtos.

Lerá e exibirá todos os produtos.

Atualizará um dos produtos.

Lerá e exibirá os produtos novamente para mostrar a atualização.

Deletará um dos produtos.

Lerá e exibirá os produtos mais uma vez para mostrar a exclusão.

Fechará a conexão com o banco de dados.

7. Estrutura da Tabela

A tabela public.produtos é criada com a seguinte estrutura:
CREATE TABLE public.produtos (
    id SERIAL PRIMARY KEY,    -- Chave primária auto-incrementável
    nome VARCHAR(100) NOT NULL, -- Nome do produto
    preco DECIMAL(10, 2) NOT NULL, -- Preço com 10 dígitos no total, 2 após a vírgula
    quantidade INT DEFAULT 0  -- Quantidade em estoque
);

8. Tecnologias Utilizadas

Java 8+

PostgreSQL

JDBC (Java Database Connectivity)

Aiven (Provedor de banco de dados em nuvem)