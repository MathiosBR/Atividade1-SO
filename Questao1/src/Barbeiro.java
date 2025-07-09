import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Barbeiro implements Runnable {
  private String nome;
  private Random random = new Random();

  public Barbeiro(String nome) {
    this.nome = nome;
  }

  @Override
  public void run() {
    System.out.println("👨‍💼 " + nome + " está pronto para trabalhar.");
    try {
      while (true) {
        // Barbeiro dorme se não houver clientes
        System.out.println("😴 " + nome + " está esperando por clientes...");
        Barbearia.clientesEsperando.acquire(); // Bloqueia até que haja um cliente

        // Barbeiro acordou, tenta pegar um cliente da fila
        // Ele precisa de uma permissão de barbeiro e de uma cadeira de espera liberada
        Barbearia.barbeirosDisponiveis.acquire(); // Garante que este barbeiro está "ativo"
        Cliente cliente = Barbearia.filaClientes.poll(100, TimeUnit.MILLISECONDS); // Tenta pegar cliente da fila

        if (cliente != null) {
          System.out.println("✂️ " + nome + " está cortando o cabelo de " + cliente.getNome() + ".");
          Barbearia.cadeirasDisponiveis.release(); // Libera uma cadeira de espera

          // Tempo de corte de cabelo aleatório entre 5s e 10s
          long tempoCorte = 5000 + random.nextInt(5001); // 5000ms a 10000ms
          TimeUnit.MILLISECONDS.sleep(tempoCorte);
          System.out.println(
              "✅ " + nome + " terminou o corte de " + cliente.getNome() + " em " + (tempoCorte / 1000.0) + "s.");
        }
        Barbearia.barbeirosDisponiveis.release(); // Barbeiro liberado (pronto para outro cliente ou dormir)
      }
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      System.out.println("❗ " + nome + " foi interrompido e parou de trabalhar.");
    }
  }
}