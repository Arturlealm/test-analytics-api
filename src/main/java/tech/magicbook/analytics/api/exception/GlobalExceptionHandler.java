package tech.magicbook.analytics.api.exception;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApplicationAlreadyExistsException.class)
    public ProblemDetail handleApplicationAlreadyExists(ApplicationAlreadyExistsException exception) {

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, exception.getMessage());

        problem.setTitle("Application already exists");

        return problem;
    }

    @ExceptionHandler(ApplicationNotFoundException.class)
    public ProblemDetail handleApplicationNotFound(ApplicationNotFoundException exception) {

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, exception.getMessage());

        problem.setTitle("Application not found");

        return problem;
    }

    @ExceptionHandler(InvalidRequestException.class)
    public ProblemDetail handleInvalidRequest(InvalidRequestException exception) {

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getMessage());

        problem.setTitle("Invalid request");

        return problem;
    }

    @ExceptionHandler(ApiKeyNotFoundException.class)
    public ProblemDetail handleApiKeyNotFound(ApiKeyNotFoundException exception) {

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, exception.getMessage());

        problem.setTitle("API key not found");

        return problem;
    }

    @ExceptionHandler(InvalidApiKeyException.class)
    public ProblemDetail handleInvalidApiKey(InvalidApiKeyException exception) {

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, exception.getMessage());

        problem.setTitle("Unauthorized");

        return problem;
    }

    @ExceptionHandler(EndUserNotFoundException.class)
    public ProblemDetail handleEndUserNotFound(EndUserNotFoundException exception) {

        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        problem.setTitle("Not Found");
        problem.setDetail(exception.getMessage());

        return problem;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidationException(MethodArgumentNotValidException exception) {

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,
                "One or more fields are invalid");

        problem.setTitle("Validation Error");

        List<String> errors = exception
                .getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .toList();

        problem.setProperty("errors", errors);

        return problem;
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ProblemDetail handleInvalidCredentials(InvalidCredentialsException ex) {

        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.UNAUTHORIZED);

        problem.setTitle("Unauthorized");
        problem.setDetail(ex.getMessage());

        return problem;
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ProblemDetail handleHttpMessageNotReadable(HttpMessageNotReadableException exception) {

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,
                "Request body contains invalid or malformed JSON");

        problem.setTitle("Invalid request");

        return problem;
    }
}
