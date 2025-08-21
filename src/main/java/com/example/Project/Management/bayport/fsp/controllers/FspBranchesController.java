package com.example.Project.Management.bayport.fsp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController("/fsp-branches")
public class FspBranchesController {

    private com.example.Project.Management.bayport.fsp.services.FinancialServiceProviderBranchService financialServiceProviderBranchService;

    @Autowired
    void setFinancialServiceProviderBranchService(com.example.Project.Management.bayport.fsp.services.FinancialServiceProviderBranchService financialServiceProviderBranchService){
        this.financialServiceProviderBranchService = financialServiceProviderBranchService;
    }

    @GetMapping(value = "/fsp-branches/{fspCode}", produces = "application/xml")
    public ResponseEntity<String> getFspBranches(@PathVariable("fspCode") String fspCode)
    {
         return financialServiceProviderBranchService.getFspBranches(fspCode);
    }

}
