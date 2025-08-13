package com.dflex.emkopo.commons.models;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class ErrorResponse extends Response{
    List<FieldError> errors;

    public ErrorResponse(HttpStatus status, String message, String path, List<FieldError> errors){
        this.status     = status;
        this.message    = message;
        this.path       = path;
        this.timestamp  = Timestamp.valueOf(LocalDateTime.now());
        this.errors     = errors;
    }
}
