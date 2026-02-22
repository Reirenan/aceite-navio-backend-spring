package br.com.treinaweb.twjobs.core.exceptions;

public class NegocioException extends RuntimeException{
    public NegocioException(String message) {
        super(message);
    }
}
