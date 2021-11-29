package co.mvpmatch.vendingmachine.clients;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;

import java.util.Collection;

import static co.mvpmatch.vendingmachine.Main.*;
import static co.mvpmatch.vendingmachine.accesscontrol.AuthenticationFilter.AUTHENTICATION_SCHEME;
import static jakarta.ws.rs.core.HttpHeaders.AUTHORIZATION;

public abstract class AbstractClient<T> {

  protected final WebTarget target;

  private String authToken;

  final GenericType<Collection<T>> collectionType = new GenericType<>() {
  };

  public AbstractClient() {
    // create the client
    Client c = ClientBuilder.newClient();
    // remember the target
    target = c.target(BASE_URI + API_NAME + API_V1);
  }

  protected T post(String path, Object body, Class<T> responseClass) {
    Invocation.Builder request = createRequest(path);
    return request.post(Entity.json(body), responseClass);
  }

  public T get(String path, Class<T> responseClass) {
    Invocation.Builder request = createRequest(path);
    return request.get(responseClass);
  }

  public T put(String path, Object body, Class<T> responseClass) {
    Invocation.Builder request = createRequest(path);
    return request.put(Entity.json(body), responseClass);
  }

  public T delete(String path, Class<T> responseClass) {
    Invocation.Builder request = createRequest(path);
    return request.delete(responseClass);
  }

  public Collection<T> getCollection(String path) {
    Invocation.Builder request = createRequest(path);
    return request.get(collectionType);
  }

  private Invocation.Builder createRequest(String path) {
    Invocation.Builder request = target.path(path).request();
    if (null != authToken) {
      request.header(AUTHORIZATION, AUTHENTICATION_SCHEME + " " + authToken);
    }
    return request;
  }

  public String getAuthToken() {
    return authToken;
  }

  public void setAuthToken(String authToken) {
    this.authToken = authToken;
  }
}
