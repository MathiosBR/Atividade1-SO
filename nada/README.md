Código referente a primeira questão da Atividade 1 de sistemas operacionais

################################################################################
Enunciado da quetão:

▸Implemente o problema do Barbeiro Dorminhoco

▸Imagine que:
▹Existem 2 barbeiros
▹A fila pode ter no máximo 10 clientes esperando
▹Caso o cliente chegue e a fila esteja cheia, exiba uma mensagem informativa

▸Modele o seu programa para que:
▹O corte de cabelo de um cliente demore um tempo aleatório entre 5s e 10s
▹Um novo cliente chegue aleatoriamente entre 4s e 6s

################################################################################
Para executar é só dar um run na classe Main.java

################################################################################

Notas de implementação:
A solução foi desenvolvida utilizando threads e mecanismos de sincronização.

Main.java: É responsável por iniciar os barbeiros e a simulação de chegada de clientes.

Barbeiro.java: Define a classe Barbeiro, que implementa a interface Runnable para ser executada como uma thread independente. Cada instância desta classe representa um barbeiro na barbearia.

Cliente.java: Uma classe simples para representar um cliente, contendo seu nome/ID.

Barbearia.java: Esta classe centraliza a lógica de sincronização e o estado compartilhado da barbearia (fila, semáforos).
