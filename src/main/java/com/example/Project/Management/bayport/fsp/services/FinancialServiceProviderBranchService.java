package com.example.Project.Management.bayport.fsp.services;

import org.springframework.http.ResponseEntity;

public interface FinancialServiceProviderBranchService{


    ResponseEntity<String> getFspBranches(String fspCode);

}
