package br.com.laps.aceite.core.exceptions;

public class JobNotFoundException extends ModelNotFoundException {

    public JobNotFoundException() {
        super("Job not found");
    }
    
    public JobNotFoundException(String message) {
        super(message);
    }
    
}
