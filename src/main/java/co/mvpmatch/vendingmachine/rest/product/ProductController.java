package co.mvpmatch.vendingmachine.rest.product;

import co.mvpmatch.vendingmachine.contracts.IProductService;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.Collection;
import java.util.Collections;

import static co.mvpmatch.vendingmachine.Main.API_NAME;
import static co.mvpmatch.vendingmachine.Main.API_V1;

@Path(API_NAME + API_V1 + "product")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ProductController {

  @Inject
  IProductService productService;

  @GET
  @Path("/{username}")
  public Collection<IProductService.Product> getBySellerUsername(@PathParam("username") String username) {
    return productService.getBySellerUsername(username);
  }

  @POST
  public IProductService.Product createProduct(IProductService.Product product) {
    return productService.createProduct(product);
  }
}
