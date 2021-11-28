package co.mvpmatch.vendingmachine.rest;

import jakarta.json.bind.annotation.JsonbCreator;
import jakarta.json.bind.annotation.JsonbProperty;

public class ErrorMessage {
  private final String message;

  @JsonbCreator
  public static ErrorMessage create(@JsonbProperty("message") String message) {
    return new ErrorMessage(message);
  }

  private ErrorMessage(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }
}
