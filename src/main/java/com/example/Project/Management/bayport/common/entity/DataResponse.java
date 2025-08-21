package com.example.Project.Management.bayport.common.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@EqualsAndHashCode(callSuper = true)
@Data
public class DataResponse<T> extends Response{
    private T data;

    public DataResponse(HttpStatus status, String message, String path, T data){
        this.status = status;
        this.message = message;
        this.path = path;
        this.timestamp = Timestamp.from(LocalDateTime.now().toInstant(ZoneOffset.ofHours(3)));
        this.data = data;
    }
}
