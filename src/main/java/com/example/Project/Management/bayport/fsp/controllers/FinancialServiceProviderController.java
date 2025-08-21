package com.example.Project.Management.bayport.fsp.controllers;

import com.example.Project.Management.bayport.common.entity.DataResponse;
import com.example.Project.Management.bayport.fsp.models.entities.FinancialServiceProvider;
import com.example.Project.Management.bayport.fsp.models.forms.FspForm;
import com.example.Project.Management.bayport.fsp.services.FinancialServiceProviderService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/fsp")
public class FinancialServiceProviderController {
    private final FinancialServiceProviderService fspService;

    @Autowired
    public FinancialServiceProviderController(FinancialServiceProviderService fspService) {
        this.fspService = fspService;
    }

    @GetMapping
    public ResponseEntity<DataResponse<List<FinancialServiceProvider>>> list(HttpServletRequest request){
        return ResponseEntity.ok(new DataResponse<>(
                HttpStatus.OK, "Financial service provider have been retrieved.",
                request.getRequestURI(), fspService.findAll()
        ));
    }

    @GetMapping("{fspId}")
    public ResponseEntity<DataResponse<Optional<FinancialServiceProvider>>> show(@PathVariable Long fspId, HttpServletRequest request){
        return ResponseEntity.ok(new DataResponse<>(
                HttpStatus.OK, "Financial service provider has been retrieved.",
                request.getRequestURI(), fspService.findById(fspId)
        ));
    }
    
}
