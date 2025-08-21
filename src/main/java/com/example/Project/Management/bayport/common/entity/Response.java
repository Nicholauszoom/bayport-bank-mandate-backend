package com.example.Project.Management.bayport.common.entity;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.sql.Timestamp;
@Data
public class Response {
    protected HttpStatus status;
    protected String message;
    protected Timestamp timestamp;
    protected String path;
}
