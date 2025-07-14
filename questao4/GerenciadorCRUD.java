package questao4;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

public class GerenciadorCRUD {
	//Substituir caso use outro BD:
	private static String urlAiven = "jdbc:postgresql://pg-para-so-academico-3527.i.aivencloud.com:22192/defaultdb?sslmode=require";
	private static String usuarioAiven = "teste";
	private static String senhaAiven = "teste123";
	
	public static void main(String[] args) {
		
		//Inicializando a disciplina de acesso ao BD
		DisciplinaAcessoBD disciBD = new DisciplinaAcessoBD(urlAiven, usuarioAiven, senhaAiven);
		
		//Criando tabela antes de iniciar as threads para garantir estado inicial limpo
		disciBD.criarTabelaProdutos();
		
		//Inserindo dados iniciais
		System.out.println("\n--- INSERINDO DADOS INICIAIS ---");
		disciBD.inserirProdutos("Produto A", new BigDecimal("100.00"), 10);
		disciBD.inserirProdutos("Produto B", new BigDecimal("200.00"), 5);
		disciBD.inserirProdutos("Produto C", new BigDecimal("300.00"), 15);
		
		//Número de threads para testar a concorrência configurado para 15
		int numThreads = 15;
		
		Thread[] threads = new Thread[numThreads];
		Random random = new Random();
		
		System.out.println("\n--- INICIANDO THREADS DE TESTE ---");
		
		for(int i = 0; i < numThreads; i++) {
			int threadId = i; // variável para identificar a thread nos logs
			threads[i] = new Thread(() -> {
				try {
					//Cada thread fará algumas operações aleatórias
					for(int j = 0; j < 5; j++) {
						int operation = random.nextInt(4);
						
						switch (operation) {
						case 0: // Operação de leitura
							disciBD.lerProdutos();
							break;
						case 1: // Operação de Inserção
							disciBD.inserirProdutos("Produto Thread" + threadId + "-" + j,
									new BigDecimal(random.nextDouble() * 1000).
									setScale(2, RoundingMode.HALF_UP), random.nextInt(50) + 1);
							break;
						case 2: // Operação de Atualização
							int idToUpdate = random.nextInt(3) + 1;
							disciBD.atualizarProduto(idToUpdate,
									"Produto Atualizado " + idToUpdate,
									new BigDecimal(random.nextDouble() * 500).
									setScale(2, RoundingMode.HALF_UP), random.nextInt(50) + 1);
							break;
						case 3: // Operação de Deleção
							// Tentando deletar IDs 1, 2 ou 3
							int idToDelete = random.nextInt(3) + 1;
							disciBD.deletarProduto(idToDelete);
							break;
						}
						Thread.sleep(random.nextInt(200) + 50);
					}
				} catch(InterruptedException e) {
					Thread.currentThread().interrupt();
					System.out.println("Thread" + threadId + " foi interrompida.");
				}
			}, "Thread-" + threadId);
			threads[i].start();
		}
		
		for (int i = 0; i < numThreads; i++) {
			try {
				threads[i].join();
			}catch(InterruptedException e) {
				Thread.currentThread().interrupt();
				System.out.println("Erro ao esperar Thread " + i + " terminar.");
			}
		}
		
		System.out.println("\n--- TODAS AS THREADS CONCLUÍRAM ---");
		
		// Leitura final para ver o estado do BD
		disciBD.lerProdutos();
		
		//Fechando a conexão com o BD
		disciBD.fecharConexao();
		
	}
}
