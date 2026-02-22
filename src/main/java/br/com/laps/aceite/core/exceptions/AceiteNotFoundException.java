package br.com.laps.aceite.core.exceptions;

public class AceiteNotFoundException extends ModelNotFoundException {
    public AceiteNotFoundException() {
        super("Aceite not found");
    }

    public AceiteNotFoundException(String message) {
        super(message);
    }
}
