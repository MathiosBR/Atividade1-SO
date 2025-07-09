import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        System.out.println("Iniciando a simulação da Barbearia Dorminhoca...");

        // 1. Criar e iniciar os barbeiros
        Thread barbeiro1 = new Thread(new Barbeiro("João"));
        Thread barbeiro2 = new Thread(new Barbeiro("Pedro"));

        barbeiro1.start();
        barbeiro2.start();

        // 2. Simular a chegada de clientes
        Random random = new Random();

        while (true) {
            try {
                // Novo cliente chega aleatoriamente entre 4s e 6s
                long tempoChegada = 4000 + random.nextInt(2001); // 4000ms a 6000ms
                TimeUnit.MILLISECONDS.sleep(tempoChegada);

                Cliente novoCliente = new Cliente();
                Barbearia.clienteChega(novoCliente);

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Simulação de chegada de clientes interrompida.");
                break;
            }
        }
    }
}