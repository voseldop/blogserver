package info.podlesov.configuration;

import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ResponseConfig {
  @ExceptionHandler(javax.validation.ConstraintViolationException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<String> constraintViolation(ConstraintViolationException ex) {
    return new ResponseEntity<>("not valid due to validation error: " + ex.getMessage(), HttpStatus.BAD_REQUEST);
  }
}
