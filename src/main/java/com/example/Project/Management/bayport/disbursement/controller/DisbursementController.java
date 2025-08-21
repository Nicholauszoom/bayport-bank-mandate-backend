package com.example.Project.Management.bayport.disbursement.controller;

import com.example.Project.Management.bayport.disbursement.dto.portal.DisbursementDto;
import com.example.Project.Management.bayport.disbursement.service.DisbursementService;
import com.example.Project.Management.bayport.product.dto.portal.ProductDto;
import com.example.Project.Management.bayport.common.enumerator.RequestAction;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/portal/disbursement")
public class DisbursementController {

    private final DisbursementService disbursementService;

    public DisbursementController(DisbursementService disbursementService) {
        this.disbursementService = disbursementService;
    }

    @PostMapping
    public ResponseEntity<?> addDisbursement(@RequestBody DisbursementDto disbursementDto) throws Exception {
        if (disbursementDto.getAction().equals(RequestAction.ACCEPT)) {
            return disbursementService.saveLoanDisbursement(disbursementDto);
        } else {
            return disbursementService.saveLoanDisbursementFailure(disbursementDto);
        }

    }
}
