package pl.coderslab.cryptomanagement.exception;

import lombok.Getter;

@Getter
public class UnmatchedPasswordsException extends RuntimeException {
    private String errorCode = "UP_1";

    public UnmatchedPasswordsException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public UnmatchedPasswordsException() {
        super("Passwords didn't match");
    }
}
