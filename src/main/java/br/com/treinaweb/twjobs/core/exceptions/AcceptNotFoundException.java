package br.com.treinaweb.twjobs.core.exceptions;

public class AcceptNotFoundException extends ModelNotFoundException{

    public AcceptNotFoundException(){super("Accept not found.");}

    public AcceptNotFoundException(String message){super(message);}


}
