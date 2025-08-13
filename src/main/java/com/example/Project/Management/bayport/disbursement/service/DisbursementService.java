package com.dflex.emkopo.app.disbursement.service;

import org.springframework.http.ResponseEntity;

public interface DisbursementService {


     ResponseEntity<String> saveLoanDisbursement(String message) throws Exception;

    ResponseEntity<String> saveLoanDisbursementFailure(String message) throws Exception;

    //BAYPORT DISBURSEMENT
    ResponseEntity<String> saveBayportLoanDisbursement(String message) throws Exception;

    ResponseEntity<String> saveBayportLoanDisbursementFailure(String message) throws Exception;

    ResponseEntity<String> takeOverLoanDisbursement(String message) throws Exception;
}
