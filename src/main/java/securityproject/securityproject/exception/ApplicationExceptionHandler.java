package securityproject.securityproject.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class ApplicationExceptionHandler {
    
    /*Questa mappa deve ritornare una coppia che ha come chiave il nome del campo che ha l'errore
     * e come valore il messaggio di errore relativo.
    */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });
        return errors;
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(UserNotFoundException.class)
    public Map<String, String> handleUserNotFoundException(UserNotFoundException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("errorMessage", ex.getMessage());
        return errors;
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(RequestNotFoundException.class)
    public Map<String, String> handleRequestNotFound(RequestNotFoundException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("errorMessage", ex.getMessage());
        return errors;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UserAlreadyExistsException.class)
    public Map<String, String> handleUserAlreadyExistsException(UserAlreadyExistsException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("errorMessage", ex.getMessage());
        return errors;
    }
    
}
