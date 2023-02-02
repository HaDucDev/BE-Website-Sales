package com.haduc.beshop.util.exception;


import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class NotXException extends RuntimeException{

    private HttpStatus httpStatus;

    public NotXException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }
}
