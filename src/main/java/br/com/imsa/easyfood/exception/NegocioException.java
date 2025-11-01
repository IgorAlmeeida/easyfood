package br.com.imsa.easyfood.exception;

public class NegocioException extends RuntimeException {
    public NegocioException(String message) {
        super(message);
    }

    public NegocioException(String message, Exception e) {
        super(message, e);
    }
}
