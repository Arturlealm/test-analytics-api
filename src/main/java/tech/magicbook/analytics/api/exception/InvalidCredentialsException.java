package tech.magicbook.analytics.api.exception;

public class InvalidCredentialsException  extends RuntimeException{

    public InvalidCredentialsException(){
        super("Invalid Credentials");
    }
    
}
