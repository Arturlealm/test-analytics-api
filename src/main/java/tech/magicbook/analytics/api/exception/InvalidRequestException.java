package tech.magicbook.analytics.api.exception;

public class InvalidRequestException extends RuntimeException {
    
    public InvalidRequestException(String message){
        super(message);
    }
}
