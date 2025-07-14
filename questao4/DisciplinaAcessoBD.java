package questao4;

import java.math.BigDecimal;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.Lock;

public class DisciplinaAcessoBD {
	private FuncoesBD funcoesBD;
	//Semáforo para limitar o número de leituras simultâneas (max de 10).
	private Semaphore readSemaphore = new Semaphore(10);
	//ReadWriteLock para garantir que apenas uma escrita ocorra por vez
	private ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
	private Lock readLock = rwLock.readLock();
	private Lock writeLock = rwLock.writeLock();
	
	public DisciplinaAcessoBD (String urlBD, String usuario, String senha) {
		this.funcoesBD = new FuncoesBD();
		if(this.funcoesBD.conectar_ao_db(urlBD, usuario, senha) == null) {
			System.err.println("Erro: Não foi possível estabelecer conexão com o BD.");
		}
	}
	
	public void fecharConexao() {
		funcoesBD.fecharConexao();
	}
	
	//Método para criar/recriar a tabela
	public void criarTabelaProdutos() {
		funcoesBD.criarTabelaProdutos();
	}
	
	// Métodos CRUD com disciplina de acesso
	
	//CREATE: Adquire o writeLock
	public void inserirProdutos(String nome, BigDecimal preco, int quantidade) {
		System.out.println(Thread.currentThread().getName() + ": Tentando adquirir lock de escrita para inserir produto...");
		writeLock.lock();
		try {
			System.out.println(Thread.currentThread().getName() + ": Lock de escrita adquirido. Inserindo produto...");
			funcoesBD.inserirProduto(nome, preco, quantidade);
		} finally {
			writeLock.unlock();
			System.out.println(Thread.currentThread().getName() + ": Lock de escrita liberado.");
		}
	}
	
	//READ: Adquire permit do semáforo e readLock
	public void lerProdutos() {
		System.out.println(Thread.currentThread().getName() + ": Tentando adquirir permit de leitura...");
		
		try {
			readSemaphore.acquire();
			System.out.println(Thread.currentThread().getName() + ": Permit de leitura adquirido. Tentando adquirir readLock...");
			readLock.lock();
			try {
				System.out.println(Thread.currentThread().getName() + ": ReadLock adquirido. Lendo produtos...");
				funcoesBD.lerProdutos();
				Thread.sleep(500);
			} finally {
				readLock.unlock();
				System.out.println(Thread.currentThread().getName() + ": ReadLock liberado.");
			}
		}catch(InterruptedException e) {
			Thread.currentThread().interrupt();
			System.out.println(Thread.currentThread().getName() + ": Leitura interrompida.");
		} finally {
			readSemaphore.release();
			System.out.println(Thread.currentThread().getName() + ": Permit de leitura liberado.");
		}
	}
	
	//UPDATE: Adquire o writeLock
	public void atualizarProduto (int id, String novoNome, BigDecimal novoPreco, int novaQuantidade) {
		System.out.println(Thread.currentThread().getName() + ": Tentando adquirir lock de escrita para atualizar produto...");
		writeLock.lock();
		try {
			System.out.println(Thread.currentThread().getName() + ": Lock de escrita adquirido. Atualizando produto...");
			funcoesBD.atualizarProduto(id, novoNome, novoPreco, novaQuantidade);
		} finally {
			writeLock.unlock();
			System.out.println(Thread.currentThread().getName() + ": Lock de escrita liberado.");
		}
	}
	
	//DELETE: Adquire o writeLock
	public void deletarProduto(int id) {
		System.out.println(Thread.currentThread().getName() + ": Tentando adquirir lock de escrita para deletar produto...");
		writeLock.lock();
		try {
			System.out.println(Thread.currentThread().getName() + ": Lock de escrita adquirido. Deletando produto...");
			funcoesBD.deletarProduto(id);
		} finally {
			writeLock.unlock();
			System.out.println(Thread.currentThread().getName() + ": Lock de escrita liberado.");
		}
	}
}
