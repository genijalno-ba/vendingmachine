package co.mvpmatch.vendingmachine;

import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;

import static org.junit.Assert.assertEquals;

public class MyResourceTest {

  private HttpServer server;
  private WebTarget target;

  @Before
  public void setUp() throws Exception {
    // start the server
    server = Main.startServer();
    // create the client
    Client c = ClientBuilder.newClient();
    // remember the target
    target = c.target(Main.BASE_URI);
  }

  @After
  public void tearDown() throws Exception {
    server.shutdownNow();
  }

  @Test
  public void testGetIt() {
    ResponseObject response = target.path("myresource").request().get(ResponseObject.class);
    assertEquals("Got it!", response.getMessage());
  }

  @Test
  public void testPostIt() {
    RequestObject request = new RequestObject();
    request.setMessage("Got it posted!");
    ResponseObject response = target.path("myresource").request().post(Entity.json(request), ResponseObject.class);
    assertEquals("Got it posted!", response.getMessage());
  }
}
