package co.mvpmatch.vendingmachine.clients;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;

import java.util.Collection;

import static co.mvpmatch.vendingmachine.Main.*;

public abstract class AbstractClient<T> {

  protected final WebTarget target;

  final GenericType<Collection<T>> collectionType = new GenericType<>() {
  };

  public AbstractClient() {
    // create the client
    Client c = ClientBuilder.newClient();
    // remember the target
    target = c.target(BASE_URI + API_NAME + API_V1);
  }

  protected T post(String path, Object body, Class<T> responseClass) {
    return target.path(path).request().post(Entity.json(body), responseClass);
  }

  public T get(String path, Class<T> responseClass) {
    return target.path(path).request().get(responseClass);
  }

  public Collection<T> getCollection(String path) {
    return target.path(path).request().get(collectionType);
  }
}
