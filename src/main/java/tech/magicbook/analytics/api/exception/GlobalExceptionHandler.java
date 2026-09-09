package tech.magicbook.analytics.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice 
public class GlobalExceptionHandler {
    
    @ExceptionHandler (ApplicationAlreadyExistsException.class)
    public ProblemDetail handleApplicationAlreadyExists(ApplicationAlreadyExistsException exception){

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, exception.getMessage());

        problem.setTitle("Application already exists");

        return problem;
    }

    @ExceptionHandler (ApplicationNotFoundException.class)
    public ProblemDetail handleApplicationNotFound(ApplicationNotFoundException exception){

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, exception.getMessage());

        problem.setTitle("Application not found");

        return problem;
    } 


    @ExceptionHandler (InvalidRequestException.class)
    public ProblemDetail handleInvalidRequest(InvalidRequestException exception){

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getMessage());

        problem.setTitle("Invalid request");

        return problem;
    }

    @ExceptionHandler (ApiKeyNotFoundException.class)
    public ProblemDetail handleApiKeyNotFound(ApiKeyNotFoundException exception){

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, exception.getMessage());

        problem.setTitle("API key not found");

        return problem;
    }
}
