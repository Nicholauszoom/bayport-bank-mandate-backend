package com.dflex.emkopo.app.setup.service;

import com.dflex.emkopo.app.setup.entity.UtumishiResponse;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public interface UtumishiResponseService {

//    UtumishiResponse saveUtumishiResponse(UtumishiResponse utumishiResponse);
    ResponseEntity<String> saveUtumishiResponse(String message) throws Exception;
    Optional<UtumishiResponse> findById(long id);
}
