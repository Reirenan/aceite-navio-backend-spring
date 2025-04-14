package br.com.treinaweb.twjobs.core.exceptions;

public class BlackListedNotFoundException extends  ModelNotFoundException{

    public BlackListedNotFoundException(){super("BlackListed not found.");}

    public BlackListedNotFoundException(String message) {
        super(message);
    }
}
