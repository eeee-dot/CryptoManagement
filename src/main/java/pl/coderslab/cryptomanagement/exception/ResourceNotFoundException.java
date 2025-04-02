package pl.coderslab.cryptomanagement.exception;

import lombok.Getter;

@Getter
public class ResourceNotFoundException extends RuntimeException {
  private String errorCode = "RNF_1";

  public ResourceNotFoundException(String message, String errorCode) {
    super(message);
    this.errorCode = errorCode;
  }

  public ResourceNotFoundException(String message) {
    super(message);
  }

  public ResourceNotFoundException(Long id) {
    super(String.format("Resource with %d not found.", id));
  }

  public String getErrorCode() {
    return errorCode;
  }
}
