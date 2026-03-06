package br.com.laps.aceite.core.exceptions;

public class VettingNotFoundException extends ModelNotFoundException {

    private static final long serialVersionUID = 1L;

    public VettingNotFoundException(String message) {
        super(message);
    }

    public VettingNotFoundException() {
        super("Vetting não encontrado");
    }

}
