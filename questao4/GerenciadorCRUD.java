package questao4;

import java.math.BigDecimal;

public class GerenciadorCRUD {
	public static void main(String[] args) {
		//Substituir caso use outro BD:
		String urlAiven = "jdbc:postgresql://pg-para-so-academico-3527.i.aivencloud.com:22192/defaultdb?sslmode=require";
		String usuarioAiven = "teste";
		String senhaAiven = "teste123";
		
		
		//Realizando a conexão com o banco de dados
		FuncoesBD db = new FuncoesBD();
		db.conectar_ao_db(urlAiven, usuarioAiven, senhaAiven);
		
		//Criando tabela
		db.criarTabelaProdutos();
		
		//Inserindo 2 produtos
		System.out.println("\n--- CRIANDO PRODUTOS ---");
		db.inserirProduto("Notebook Gamer", new BigDecimal("7500.50"), 5);
		db.inserirProduto("Mouse sem fio", new BigDecimal ("120.00"), 20);
		
		//Lendo todos os produtos
		System.out.println("\n--- LENDO TODOS OS PRODUTOS");
		db.lerProdutos();
		
		//Atualizando um produto
		//OBS.: É necessário ler antes os produtos para pegar o ID correto
		System.out.println("\n--- ATUALIZANDO PRODUTO ---");
		db.atualizarProduto(1, "Notebook Gamer Pro", new BigDecimal ("8000.00"), 4);
		
		//Lendo todos os produtos após atualização
		System.out.println("\n--- LENDO PRODUTOS APÓS ATUALIZAÇÃO ---");
		db.lerProdutos();
		
		//Deletando um produto
		System.out.println("\n--- DELETANDO PRODUTO ---");
		db.deletarProduto(2);
		
		//Lendo produtos após exclusão
		System.out.println("\n--- LENDO PRODUTOS APÓS EXCLUSÃO ---");
		db.lerProdutos();
		
		//Fechando conexão
		db.fecharConexao();
	}
}