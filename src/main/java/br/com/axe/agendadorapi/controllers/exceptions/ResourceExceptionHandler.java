package br.com.axe.agendadorapi.controllers.exceptions;

import java.time.LocalDateTime;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import br.com.axe.agendadorapi.services.exceptions.DataIntegrityViolationException;
import br.com.axe.agendadorapi.services.exceptions.ObjectNotFoundException;

@ControllerAdvice
public class ResourceExceptionHandler {
    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<StandardError> objectNotFound(
            ObjectNotFoundException ex,
            HttpServletRequest req
    ) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                StandardError.builder().
                        timestamp(LocalDateTime.now()).
                        status(HttpStatus.NOT_FOUND.value()).
                        error(ex.getMessage()).
                        path(req.getRequestURI()).
                        build()
        );
    }
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<StandardError> emailAlreadyExists(
            DataIntegrityViolationException ex,
            HttpServletRequest req
    ) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                StandardError.builder().
                        timestamp(LocalDateTime.now()).
                        status(HttpStatus.BAD_REQUEST.value()).
                        error(ex.getMessage()).
                        path(req.getRequestURI()).
                        build()
        );
    }
}
