package tech.magicbook.analytics.api.exception;

public class EndUserNotFoundException extends RuntimeException {
    
    public EndUserNotFoundException(){
        super("End user not found");
    }
}
