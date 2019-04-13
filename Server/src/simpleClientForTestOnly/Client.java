import java.net.*;
import java.io.*;

public class Client extends Thread {

  private Socket sock = null;
  private PrintWriter out = null;
  private BufferedReader in = null;
  private String nameToSearch;

  public Client(String host, int port, String name ) {
    try {
      sock = new Socket(host, port);
      out = new PrintWriter(sock.getOutputStream(), true);
      in = new BufferedReader(
               new InputStreamReader(
                   sock.getInputStream()));

      nameToSearch = name;

    } catch (Exception exc) {
        exc.printStackTrace();
        System.exit(4);
    }
    start();
  }

  public void run() {
    try {
      String message = "{\"action\": \"HANDSHAKE\",\"client_name\": \"some_name\",\"payload\": \"empty_payload\"}";
      out.println(message);
      System.out.println("clinet send message");
      Thread.sleep(500);
      
      //out.println("bye");
    } catch (Exception exc) {
        exc.printStackTrace();
    }
  }

  private void find(String name) throws IOException {
    out.println("get " + name);
    
  }

  public static void main(String[] args) {
    
    
    String host = "127.0.0.1";
    int port = 8080;
    String[] names = { "Test1", "Test2", "Test3" };
    for (int i=0; i<names.length; i++)
      new Client(host, port, names[i]);
  }
}
