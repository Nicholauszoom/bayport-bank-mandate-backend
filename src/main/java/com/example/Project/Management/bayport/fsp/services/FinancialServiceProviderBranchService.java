package com.dflex.emkopo.app.fsp.services;


import org.springframework.http.ResponseEntity;

public interface FinancialServiceProviderBranchService{


    ResponseEntity<String> getFspBranches(String fspCode);

}
