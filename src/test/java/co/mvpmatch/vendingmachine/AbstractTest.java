package co.mvpmatch.vendingmachine;

import org.glassfish.grizzly.http.server.HttpServer;

public abstract class AbstractTest {

  protected static HttpServer server;

  protected static void startServer() {
    server = Main.startServer();
  }

  protected static void shutdownServer() {
    server.shutdownNow();
  }
}
