import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.example.ClientSocketHandler;

public class LoadBalancer {

  public static void main(String[] args) throws IOException {
    ServerSocket serverSocket = new ServerSocket(8081);
    System.out.println("LoadBalancer started at port: " + 8081);

    while (true) {
      Socket socket = serverSocket.accept();
      System.out.println("TCP Connection established with client :" + socket.toString());
      handleSocket(socket);

    }
  }

private static void handleSocket(Socket socket) {
  ClientSocketHandler clientSocketHandler = new ClientSocketHandler(socket);
  Thread clientSocketHandlerThread = new Thread(clientSocketHandler)
    clientSocketHandlerThread.start();
}

}
