package tech.magicbook.analytics.api.exception;

public class ApplicationAlreadyExistsException extends RuntimeException {
    
    public ApplicationAlreadyExistsException(String name){
        super("Application already exists with name: " + name);
    }
}
