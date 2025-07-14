Projeto Java CRUD Concorrente com PostgreSQL
--------------------------
Este é um projeto em Java que demonstra operações básicas de CRUD (Create, Read, Update, Delete) em um banco de dados PostgreSQL, com um foco especial no **gerenciamento de acesso concorrente** utilizando conceitos de Sistemas Operacionais como Semáforos e Locks de Leitura/Escrita.

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

Este projeto Java ilustra como estabelecer uma conexão JDBC com um banco de dados PostgreSQL (hospedado na Aiven, com conexão SSL obrigatória) e, crucialmente, como **disciplinar o acesso concorrente** a esse banco de dados. Ele simula um ambiente onde múltiplas operações (leituras e escritas) tentam acessar o banco simultaneamente, aplicando regras específicas para garantir a integridade e a ordem das operações.

2. Funcionalidades

* Conexão segura com PostgreSQL usando SSL (`sslmode=require`).
* Criação/Recriação automática da tabela `public.produtos` ao iniciar a aplicação (útil para desenvolvimento e testes).
* Operações CRUD para a entidade `Produto` (ID, Nome, Preço, Quantidade).
* **Controle de Concorrência Implementado:**
    * Máximo de **10 consultas (operações de leitura)** simultâneas.
    * Apenas **1 operação de escrita (inserção, atualização ou exclusão)** pode ocorrer simultaneamente.
    * **Nenhuma consulta pode ocorrer** enquanto uma operação de escrita estiver em andamento.
    * Threads que tentam violar essas regras são bloqueadas até que o acesso seja permitido.

3. Pré-requisitos

Para rodar este projeto, você precisará ter o seguinte instalado:

* **Java Development Kit (JDK) 8 ou superior**.
* **Apache Maven** (para gerenciamento de dependências, se estiver usando um projeto Maven).
* **Um servidor PostgreSQL** acessível (neste projeto, utilizamos a Aiven).
* **pgAdmin** (opcional, mas recomendado para gerenciar seu banco de dados e usuários).

4. Configuração do Banco de Dados

    1.  **Crie um servidor PostgreSQL** na Aiven (ou em seu ambiente local).
    2.  **Crie um usuário** (ex: `teste`) no seu banco de dados PostgreSQL usando o pgAdmin ou comandos SQL.
        * No pgAdmin, clique direito em "Login/Group Roles" > "Create" > "Login/Group Role...". Defina um nome de usuário e uma senha.
    3.  **Conceda a permissão `CONNECT`** ao seu usuário no banco de dados desejado (ex: `defaultdb`).
        * No pgAdmin, clique direito no seu banco de dados > "Properties..." > Aba "Privileges". Adicione seu usuário e marque `CONNECT`.
    4.  **Conceda a permissão `CREATE`** ao seu usuário no schema `public`. Isso é crucial para que a aplicação possa criar a tabela.
        * No pgAdmin (conectado como superusuário), execute:
            ```sql
            GRANT CREATE ON SCHEMA public TO seu_usuario;
            ```
            (Substitua `seu_usuario` pelo nome do seu usuário, ex: `teste`).
    5.  **Obtenha as credenciais de conexão** da Aiven: Host, Porta, Nome do Banco de Dados, Usuário e Senha.

6. Como Executar

    1.  Compile o projeto (se estiver usando Maven, `mvn clean install`).
    2.  Execute a classe principal `br.crud.GerenciadorCRUD`.
    
    Ao executar, a aplicação fará o seguinte:
    
    * Inicializará a `DisciplinaAcessoBD`, que se conectará ao banco de dados.
    * Deletará a tabela `produtos` (se existir) e a recriará para garantir um estado limpo.
    * Inserirá alguns produtos iniciais.
    * **Criará múltiplas threads (simulando clientes/processos concorrentes)** que tentarão realizar operações CRUD aleatórias (leitura, inserção, atualização, exclusão).
    * Você observará no console as mensagens de log detalhadas, indicando quando as threads:
        * **Tentam adquirir locks** (de leitura ou escrita).
        * **Adquirem locks** (e o tipo de lock).
        * **São bloqueadas** devido às regras de concorrência (ex: mais de 10 leituras, ou uma escrita em andamento).
        * **Liberam locks** após a conclusão de suas operações.
    * Ao final, todas as threads serão aguardadas, e uma leitura final do banco de dados será realizada para mostrar o estado final.
    
    Este teste demonstrará visualmente como o `Semaphore` limita as leituras e como o `ReentrantReadWriteLock` garante a exclusividade das escritas e a ausência de leituras durante essas operações críticas.

7. Estrutura da Tabela

A tabela public.produtos é criada com a seguinte estrutura:
CREATE TABLE public.produtos (
    id SERIAL PRIMARY KEY,    -- Chave primária auto-incrementável
    nome VARCHAR(45) NOT NULL, -- Nome do produto (máximo 45 caracteres)
    preco DECIMAL(10, 2) NOT NULL, -- Preço com 10 dígitos no total, 2 após a vírgula
    quantidade INT DEFAULT 0  -- Quantidade em estoque
);

8. Tecnologias Utilizadas

Java 8+

PostgreSQL

JDBC (Java Database Connectivity)

Aiven (Provedor de banco de dados em nuvem)

java.util.concurrent.Semaphore: Para controle de acesso com contagem de permissões.

java.util.concurrent.locks.ReentrantReadWriteLock: Para gerenciamento de locks de leitura/escrita.
