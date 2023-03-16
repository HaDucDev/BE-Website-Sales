package com.haduc.beshop.util.exception;


import com.haduc.beshop.util.dto.response.account.MessageResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult()
                .getAllErrors()
                .forEach(error -> {
                    System.out.println(error);
                    String fieldName = ((FieldError) error).getField();
                    String errorMessage = error.getDefaultMessage();
                    errors.put(fieldName, errorMessage);
                });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(NotXException.class)
    public ResponseEntity<MessageResponse> handleNotXException(NotXException exception) {
        return ResponseEntity.status(exception.getHttpStatus())
                .body(new MessageResponse(exception.getMessage()));
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<MessageResponse> handleUniqueConstraintException
            (SQLIntegrityConstraintViolationException exception)
            throws SQLIntegrityConstraintViolationException {

        if (exception.getMessage().contains("'user.unique_username_constraint'")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new MessageResponse("Tên người dùng đã tồn tại"));

        } else if (exception.getMessage().contains("'user.unique_email_constraint'")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new MessageResponse("Email đã được sử dụng"));

        }
        throw exception;

    }
}
