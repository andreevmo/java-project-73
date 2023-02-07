package hexlet.code.app.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class ExceptionHandlerApi {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<String> incorrectInputException(ConstraintViolationException e) {
        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body("Incorrect input");
    }

    @ExceptionHandler(org.hibernate.exception.ConstraintViolationException.class)
    public ResponseEntity<String> incorrectInputException(org.hibernate.exception.ConstraintViolationException e) {
        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body("Incorrect input");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> incorrectInputException(MethodArgumentNotValidException e) {
        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body("Incorrect input");
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> objectNotFound(NoSuchElementException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body("Object not found");
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<String> objectNotFound(AuthenticationException e) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body("Access denied");
    }

}
