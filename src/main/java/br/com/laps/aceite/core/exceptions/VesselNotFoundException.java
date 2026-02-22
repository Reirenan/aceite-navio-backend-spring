package br.com.treinaweb.twjobs.core.exceptions;

public class VesselNotFoundException extends ModelNotFoundException{

    public VesselNotFoundException(){ super("A embarcação não foi encontrada."); }
    public VesselNotFoundException(String message){ super(message); }

}
