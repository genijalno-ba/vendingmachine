package co.mvpmatch.vendingmachine;

import co.mvpmatch.vendingmachine.cdi.AutoScanFeature;
import co.mvpmatch.vendingmachine.rest.GenericExceptionMapper;
import co.mvpmatch.vendingmachine.rest.product.ProductController;
import co.mvpmatch.vendingmachine.rest.user.UserController;
import co.mvpmatch.vendingmachine.rest.user.UserExceptionMapper;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Main class.
 */
public class Main {

  public static final String API_NAME = "vendingmachine-api/";
  public static final String API_V1 = "v1/";

  public static final String BASE_URI = "http://localhost:8080/";

  // Starts Grizzly HTTP server
  public static HttpServer startServer() {

    final ResourceConfig config = new ResourceConfig();
    config.register(UserController.class);
    config.register(ProductController.class);
    config.register(GenericExceptionMapper.class);
    config.register(UserExceptionMapper.class);

    // enable the auto scanning
    config.register(AutoScanFeature.class);

    return GrizzlyHttpServerFactory
        .createHttpServer(URI.create(BASE_URI), config);

  }

  public static void main(String[] args) {

    try {

      final HttpServer httpServer = startServer();

      // add jvm shutdown hook
      Runtime.getRuntime().addShutdownHook(new Thread(() -> {
        try {
          System.out.println("Shutting down the application...");

          httpServer.shutdownNow();

          System.out.println("Done, exit.");
        } catch (Exception e) {
          Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, e);
        }
      }));

      System.out.printf("Application started.%nStop the application using CTRL+C%n");

      // block and wait shut down signal, like CTRL+C
      Thread.currentThread().join();

    } catch (InterruptedException ex) {
      Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
    }

  }
}
