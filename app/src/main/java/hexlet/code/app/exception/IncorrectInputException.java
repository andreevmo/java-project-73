package hexlet.code.app.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class IncorrectInputException extends RuntimeException {

    public IncorrectInputException(String message) {
        super(message);
    }
}
