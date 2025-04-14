package br.com.treinaweb.twjobs.core.exceptions;



public class BercoNotFoundException extends ModelNotFoundException{

    public BercoNotFoundException(){ super("Berço não encontrado."); }
    public BercoNotFoundException(String message) {super(message);}
}
