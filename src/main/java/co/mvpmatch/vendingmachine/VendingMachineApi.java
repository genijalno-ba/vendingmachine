package co.mvpmatch.vendingmachine;

import co.mvpmatch.vendingmachine.accesscontrol.AuthenticationFilter;
import co.mvpmatch.vendingmachine.accesscontrol.AuthorizationFilter;
import co.mvpmatch.vendingmachine.cdi.AutoScanFeature;
import co.mvpmatch.vendingmachine.data.LiquibaseFeature;
import co.mvpmatch.vendingmachine.rest.GenericExceptionMapper;
import co.mvpmatch.vendingmachine.rest.transaction.TransactionController;
import co.mvpmatch.vendingmachine.rest.deposit.DepositController;
import co.mvpmatch.vendingmachine.rest.product.ProductController;
import co.mvpmatch.vendingmachine.rest.tokensession.TokenSessionController;
import co.mvpmatch.vendingmachine.rest.user.UserController;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * VendingMachineApi class.
 */
public class VendingMachineApi {

  public static final String API_NAME = "vendingmachine-api/";
  public static final String API_V1 = "v1/";

  public static final String BASE_URI = "http://localhost:8080/";

  // Starts Grizzly HTTP server
  public static HttpServer startServer() {
    final ResourceConfig config = new ResourceConfig();
    // enable the auto scanning for contracts and services
    config.register(AutoScanFeature.class);
    // add others manually
    config.register(AuthenticationFilter.class);
    config.register(AuthorizationFilter.class);
    config.register(LiquibaseFeature.class);
    config.register(TokenSessionController.class);
    config.register(UserController.class);
    config.register(ProductController.class);
    config.register(DepositController.class);
    config.register(TransactionController.class);
    config.register(GenericExceptionMapper.class);
    // start http server
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
          Logger.getLogger(VendingMachineApi.class.getName()).log(Level.SEVERE, null, e);
        }
      }));
      System.out.printf("Application started.%nStop the application using CTRL+C%n");
      // block and wait shut down signal, like CTRL+C
      Thread.currentThread().join();
    } catch (InterruptedException ex) {
      Logger.getLogger(VendingMachineApi.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

}
