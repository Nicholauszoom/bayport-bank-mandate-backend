package com.example.Project.Management.bayport.disbursement.controller;

import com.example.Project.Management.bayport.disbursement.dto.portal.DisbursementDto;
import com.example.Project.Management.bayport.disbursement.service.DisbursementService;
import com.example.Project.Management.bayport.product.dto.portal.ProductDto;
import com.example.Project.Management.bayport.common.enumerator.RequestAction;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/disbursement")
public class DisbursementController {

    private final DisbursementService disbursementService;

    public DisbursementController(DisbursementService disbursementService) {
        this.disbursementService = disbursementService;
    }

    @PostMapping("/{id}/disburse")
    public ResponseEntity<?> loanDisburse(
            @PathVariable Long id,
            @RequestParam RequestAction requestAction,
            @RequestBody DisbursementDto disbursementDto) throws Exception {
        return disbursementService.saveLoanDisbursement(id, requestAction, disbursementDto.getReason());
    }

}
