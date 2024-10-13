package org.example;

import org.example.utils.BackendServers;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ClientSocketHandler implements Runnable {

  private Socket clientSocket;

  public ClientSocketHandler(final Socket socket) {
    this.clientSocket = socket;
  }

  @Override
  public void run() {
    try {
      InputStream clientToLBInputStream = clientSocket.getInputStream();
      OutputStream lbToClientOutputStream = clientSocket.getOutputStream();

      // Set routing strategy
      // BackendServers.setStrategy(BackendServers.LoadBalancingStrategy.LEAST_CONNECTIONS)

      String backendHost = BackendServers.getHost();
      System.out.println("Host selected to handle this request : " + backendHost);

      // Increment connection count
      BackendServers.incrementConnection(backendHost);

      Socket socket = new Socket(backendHost, 8080);

      InputStream serverToLBInputStream = backendSocket.getInputStream();
      OutputStream lbToServerOutputStream = backendSocket.getOutputStream();

      Thread clientDataHandler = new Thread() {
        @Override
        public void run() {
          try {
            int data;
            while ((data = clientToLBInputStream.read()) != -1) {
              lbToServerOutputStream.write(data);

            }
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
      };
      clientDataHandler.start();

      Thread backendDataHandler = new Thread() {
        @Override
        public void run() {
          try {
            int data;
            while ((data = serverToLBInputStream.read()) != -1) {
              lbToClientOutputStream.write(data);

            }
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
      };
      backendDataHandler.start();

    } catch (IOException e) {
      throw new RuntimeException(e);
    } finally {
      // Decrement and close connection
      BackendServers.decrementConnection(backendHost);
    }
  }
}
