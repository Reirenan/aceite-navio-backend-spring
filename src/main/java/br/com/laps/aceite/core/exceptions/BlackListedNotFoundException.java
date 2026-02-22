package br.com.laps.aceite.core.exceptions;

public class BlackListedNotFoundException extends ModelNotFoundException {

    public BlackListedNotFoundException(){super("BlackListed not found.");}

    public BlackListedNotFoundException(String message) {
        super(message);
    }
}
