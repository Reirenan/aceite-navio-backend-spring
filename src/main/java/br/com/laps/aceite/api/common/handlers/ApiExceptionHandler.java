package br.com.laps.aceite.api.common.handlers;

import br.com.laps.aceite.api.common.dtos.ErrorResponse;
import br.com.laps.aceite.api.common.dtos.ValidationErrorResponse;
import br.com.laps.aceite.core.exceptions.JwtServiceException;
import br.com.laps.aceite.core.exceptions.ModelNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(ModelNotFoundException.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public ErrorResponse handleModelNotFoundException(
        ModelNotFoundException exception
    ) {
        return ErrorResponse.builder()
            .message(exception.getLocalizedMessage())
            .build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ValidationErrorResponse handleMethodArgumentNotValidException(
        MethodArgumentNotValidException exception
    ) {
        var errors = exception.getBindingResult().getFieldErrors()
            .stream()
            .collect(Collectors.groupingBy(
                fieldError -> fieldError.getField(),
                Collectors.mapping(
                    fieldError -> fieldError.getDefaultMessage(),
                    Collectors.toList()
                )
            ));
        return ValidationErrorResponse.builder()
            .message("Validation error")
            .errors(errors)
            .build();
    }

    @ExceptionHandler(JwtServiceException.class)
    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleJwtServiceException(JwtServiceException e) {
        return ErrorResponse.builder()
            .message(e.getLocalizedMessage())
            .build();
    }
    
}
