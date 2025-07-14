package questao4;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class FuncoesBD {
	private Connection conexao = null;
	
	public Connection conectar_ao_db(String urlBD, String usuario, String senha) {		

		try {
			//Carregando driver JDBC do postgresql
			Class.forName("org.postgresql.Driver");
			this.conexao = DriverManager.getConnection(urlBD, usuario, senha);
			if(this.conexao!=null) {
				System.out.println("BD carregado e conectado!");
			}
			else {
				System.out.println("Falha de conexão!");
			}
			
		}catch (Exception e) {
			System.out.println("Erro ao conectar ao BD: " + e.getMessage());
		}
		return this.conexao;
	}
	
	public void fecharConexao() {
		if(this.conexao !=null) {
			try {
				conexao.close();
				System.out.println("Conexão com o BD fechada.");
			}catch(SQLException e) {
				System.out.println("Erro ao fechar a conexão: " + e.getMessage());
			}
		}
	}
	
	//Método para criação de uma tabela
	public void criarTabelaProdutos() {
		try(Statement declaracao = conexao.createStatement()) {
			String query = 
					"DROP TABLE IF EXISTS public.produtos; "
		          + "CREATE TABLE public.produtos "
		          + "(id SERIAL PRIMARY KEY, "
		          + "nome VARCHAR(45) NOT NULL, "
		          + "preco DECIMAL(10,2) NOT NULL, "
		          + "quantidade INT DEFAULT 0"
		          + ");";
			declaracao.executeUpdate(query);
			System.out.println("Tabela criada!");
		}catch(SQLException e) {
			System.out.println("Erro ao criar a tabela: " + e.getMessage());
		}
	}
	
	//MÉTODOS CRUD
	
	//CREATE: Inserir um novo produto
	public void inserirProduto (String nome, BigDecimal preco, int quantidade) {
		String query =
				"INSERT INTO public.produtos "
				+ "(nome, preco, quantidade) VALUES (?, ?, ?)";
		try(PreparedStatement prepDeclaracao = conexao.prepareStatement(query)){
			prepDeclaracao.setString(1, nome);
			prepDeclaracao.setBigDecimal(2, preco);
			prepDeclaracao.setInt(3, quantidade);
			int linhasAfetadas = prepDeclaracao.executeUpdate();
			if(linhasAfetadas > 0) {
				System.out.println("Produto '" + nome + "' inserido com sucesso!");
			}
		}catch (SQLException e) {
			System.out.println("Erro ao inserir produto: " + e.getMessage());
		}
	}
	
	//READ : Ler todos os produtos inseridos
	public void lerProdutos() {
		String query = 
				  "SELECT id, nome, preco, quantidade "
				+ "FROM public.produtos";
		try (PreparedStatement prepDeclaracao = conexao.prepareStatement(query);
			 ResultSet rs = prepDeclaracao.executeQuery()){
			System.out.println("\n--- LISTA DE PRODUTOS ---");
			if(!rs.isBeforeFirst()) { //Verifica se o ResultSet está vazio
				System.out.println("Nenhum produto encontrado.");
			}
			while(rs.next()) {
				int id = rs.getInt("id");
				String nome = rs.getString("nome");
				BigDecimal preco = rs.getBigDecimal("preco");
				int quantidade = rs.getInt("quantidade");
				System.out.println(
						"ID: " + id +
						", Nome: " + nome +
						", Preço: " + preco +
						", Quantidade: " + quantidade);
			}
			System.out.println("-----------------------\n");
		}catch(SQLException e) {
			System.out.println("Erro ao ler produtos: " + e.getMessage());
		}
	}
	
	//UPDATE: Atualizar um produto existente pelo ID
	public void atualizarProduto (int id, String novoNome, BigDecimal novoPreco, int novaQuantidade) {
		String query = 
				"UPDATE public.produtos SET "
				+ "nome = ?, "
				+ "preco = ?, "
				+ "quantidade = ? "
				+ "WHERE id = ?";
		try(PreparedStatement prepDeclaracao = conexao.prepareStatement(query)){
			prepDeclaracao.setString(1, novoNome);
			prepDeclaracao.setBigDecimal(2, novoPreco);
			prepDeclaracao.setInt(3, novaQuantidade);
			prepDeclaracao.setInt(4, id);
			int linhasAfetadas = prepDeclaracao.executeUpdate();
			if(linhasAfetadas > 0) {
				System.out.println("Produto ID " + id + " atualizado com sucesso!");
			}
			else {
				System.out.println("Produto ID " + id + " não encontrado para atualização.");
			}
		}catch(SQLException e) {
			System.out.println("Erro ao atualizar produto: " + e.getMessage());
		}
	}
	
	//DELETE: Deletar um produto pelo ID
	public void deletarProduto (int id) {
		String query =
				"DELETE FROM public.produtos "
			  + "WHERE id = ?";
		try(PreparedStatement prepDeclaracao = conexao.prepareStatement(query)){
			prepDeclaracao.setInt(1, id);
			int linhasAfetadas = prepDeclaracao.executeUpdate();
			if(linhasAfetadas > 0) {
				System.out.println("Produto ID " + id + " deletado com sucesso!");
			}
			else {
				System.out.println("Produto ID " + id + " não encontrado para exclusão.");
			}
		}catch(SQLException e) {
			System.out.println("Erro ao deletar produto: " + e.getMessage());
		}
	}

}
