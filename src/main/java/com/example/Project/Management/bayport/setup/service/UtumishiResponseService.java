package com.example.Project.Management.bayport.setup.service;

import com.example.Project.Management.bayport.setup.entity.UtumishiResponse;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public interface UtumishiResponseService {

//    UtumishiResponse saveUtumishiResponse(UtumishiResponse utumishiResponse);
    ResponseEntity<String> saveUtumishiResponse(String message) throws Exception;
    Optional<UtumishiResponse> findById(long id);
}
