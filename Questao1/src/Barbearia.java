import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;

public class Barbearia {
  // Semáforo para controlar o número de barbeiros disponíveis
  public static Semaphore barbeirosDisponiveis = new Semaphore(2); // 2 barbeiros
  // Semáforo para controlar o número de cadeiras de espera disponíveis
  public static Semaphore cadeirasDisponiveis = new Semaphore(10); // 10 cadeiras
  // Semáforo para indicar se há clientes esperando (barbeiro dorme/acorda)
  public static Semaphore clientesEsperando = new Semaphore(0); // Começa em 0 (nenhum cliente)

  // Fila para os clientes (thread-safe)
  public static BlockingQueue<Cliente> filaClientes = new LinkedBlockingQueue<>(10); // Fila com capacidade máxima de 10

  public static void clienteChega(Cliente cliente) {
    try {
      System.out.println("⏰ " + cliente.getNome() + " chegou na barbearia. (" + filaClientes.size() + " na fila)");

      // Tenta pegar uma cadeira de espera
      if (cadeirasDisponiveis.tryAcquire(1)) { // Tenta adquirir 1 permissão de cadeira
        filaClientes.put(cliente); // Adiciona o cliente à fila (bloqueia se a fila estiver cheia, mas o tryAcquire
                                   // já lida com isso)
        System.out
            .println("➡️ " + cliente.getNome() + " sentou para esperar. Clientes na fila: " + filaClientes.size());
        clientesEsperando.release(); // Libera uma permissão para o barbeiro saber que há um cliente
      } else {
        System.out.println("❌ " + cliente.getNome() + " encontrou a fila cheia e foi embora.");
      }
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt(); // Restaura o status de interrupção
      System.out.println("❗ " + cliente.getNome() + " foi interrompido ao tentar entrar na fila.");
    }
  }
}