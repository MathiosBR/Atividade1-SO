public class Cliente {
  private String nome;
  private int id;
  private static int proximoId = 0;

  public Cliente() {
    this.id = proximoId++;
    this.nome = "Cliente-" + this.id;
  }

  public String getNome() {
    return nome;
  }

  public int getId() {
    return id;
  }
}