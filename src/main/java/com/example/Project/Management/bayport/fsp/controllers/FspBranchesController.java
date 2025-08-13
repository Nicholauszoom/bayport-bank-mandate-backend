package com.dflex.emkopo.app.fsp.controllers;



import com.dflex.emkopo.app.fsp.services.FinancialServiceProviderBranchService;
import com.dflex.emkopo.commons.models.DataResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController("/fsp-branches")
public class FspBranchesController {


    private FinancialServiceProviderBranchService financialServiceProviderBranchService;

    @Autowired
    void setFinancialServiceProviderBranchService(FinancialServiceProviderBranchService financialServiceProviderBranchService){
        this.financialServiceProviderBranchService = financialServiceProviderBranchService;
    }

    @GetMapping(value = "/fsp-branches/{fspCode}", produces = "application/xml")
    public ResponseEntity<String> getFspBranches(@PathVariable("fspCode") String fspCode)
    {
         return financialServiceProviderBranchService.getFspBranches(fspCode);
    }

//    @PostMapping()
//    public ResponseEntity<DataResponse<?>> create(@Validated @RequestBody ){
//
//    }
}
