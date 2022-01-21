package com.example.emenu.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@RestControllerAdvice
public class AdviceController {

    @ResponseStatus(BAD_REQUEST)
    @ResponseBody
    @ExceptionHandler(value = GlobalException.class)
    public Map<String, Object> handleGenericNotFoundException(GlobalException e) {
        Map<String, Object> body = createErrorBody(e, BAD_REQUEST);
//        e.printStackTrace();
        return body;
    }

    @ResponseStatus(BAD_REQUEST)
    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, Object> methodArgumentNotValidException(MethodArgumentNotValidException ex) {
        return createErrorBody(ex, BAD_REQUEST);
    }

    @ResponseStatus(UNAUTHORIZED)
    @ResponseBody
    @ExceptionHandler(AuthenticationException.class)
    public Map<String, Object> authenticationExceptionHandler(AuthenticationException e) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", UNAUTHORIZED);
        body.put("error", "AuthenticationException");
        body.put("message", e.getMessage());
        return body;
    }

    private Map<String, Object> createErrorBody(GlobalException e, HttpStatus httpStatus) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", httpStatus);
        body.put("error", e.getErrorName());
        body.put("message", e.getMessage());
        return body;
    }

    private Map<String, Object> createErrorBody(MethodArgumentNotValidException e, HttpStatus httpStatus) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", httpStatus);
        body.put("error", "MethodArgumentNotValidException");
        body.put("message", e.getMessage());

        BindingResult result = e.getBindingResult();
        List<FieldError> fieldErrors = result.getFieldErrors();
        List<String> fieldExceptions = fieldErrors.stream()
                .map(fe -> "Field: " + fe.getField() + " is not valid! " + fe.getDefaultMessage()).collect(
                        Collectors.toList());
        body.put("field errors", fieldExceptions);
        return body;
    }
}
