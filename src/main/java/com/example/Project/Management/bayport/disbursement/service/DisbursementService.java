package com.example.Project.Management.bayport.disbursement.service;

import com.example.Project.Management.bayport.disbursement.dto.portal.DisbursementDto;
import org.springframework.http.ResponseEntity;

public interface DisbursementService {


     ResponseEntity<?> saveLoanDisbursement(DisbursementDto disbursementDto) throws Exception;

    ResponseEntity<?> saveLoanDisbursementFailure(DisbursementDto disbursementDto) throws Exception;

    //BAYPORT DISBURSEMENT
    ResponseEntity<String> saveBayportLoanDisbursement(String message) throws Exception;

    ResponseEntity<String> saveBayportLoanDisbursementFailure(String message) throws Exception;

    ResponseEntity<?> takeOverLoanDisbursement(DisbursementDto disbursementDto) throws Exception;
}
