package co.mvpmatch.vendingmachine;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("myresource")
public class MyResource {

  /**
   * Method handling HTTP GET requests. The returned object will be sent
   * to the client as "text/plain" media type.
   *
   * @return String that will be returned as a text/plain response.
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public ResponseObject getIt() {
    ResponseObject response = new ResponseObject();
    response.setMessage("Got it!");
    return response;
  }

  @POST
  @Produces(MediaType.APPLICATION_JSON)
  public ResponseObject postIt(RequestObject request) {
    ResponseObject response = new ResponseObject();
    response.setMessage(request.getMessage());
    return response;
  }
}
