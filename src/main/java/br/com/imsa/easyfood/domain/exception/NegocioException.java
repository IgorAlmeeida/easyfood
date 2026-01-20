package br.com.imsa.easyfood.domain.exception;

public class NegocioException extends RuntimeException {
    public NegocioException(String message) {
        super(message);
    }

    public NegocioException(String message, Exception e) {
        super(message, e);
    }
}
