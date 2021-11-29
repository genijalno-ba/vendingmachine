package co.mvpmatch.vendingmachine.rest.product;

import co.mvpmatch.vendingmachine.accesscontrol.Secured;
import co.mvpmatch.vendingmachine.contracts.IProductService;
import co.mvpmatch.vendingmachine.contracts.IUserService;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.math.BigInteger;
import java.util.Collection;

import static co.mvpmatch.vendingmachine.Main.API_NAME;
import static co.mvpmatch.vendingmachine.Main.API_V1;

@SuppressWarnings("unused")
@Path(API_NAME + API_V1 + "product")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ProductController {

  @Inject
  IProductService productService;

  @GET
  public Collection<IProductService.Product> getAll() {
    return productService.getAll();
  }

  @GET
  @Path("/{productId}")
  public IProductService.Product getByProductId(@PathParam("productId") BigInteger productId) {
    return productService.getByProductId(productId);
  }

  @POST
  @Secured({IUserService.Role.SELLER})
  public IProductService.Product createProduct(IProductService.ProductContext productContext) {
    return productService.createProduct(productContext);
  }

  @PUT
  @Secured({IUserService.Role.SELLER})
  public IProductService.Product updateProduct(IProductService.Product product) {
    return productService.updateProduct(product);
  }

  @DELETE
  @Path("{productId}")
  @Secured({IUserService.Role.SELLER})
  public IProductService.Product deleteProduct(@PathParam("productId") BigInteger productId) {
    return productService.deleteProduct(productId);
  }
}
