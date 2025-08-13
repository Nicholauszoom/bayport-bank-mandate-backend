package com.dflex.emkopo.app.fsp.controllers;

import com.dflex.emkopo.app.fsp.models.entities.FinancialServiceProvider;
import com.dflex.emkopo.app.fsp.models.entities.FinancialServiceProviderCategory;
import com.dflex.emkopo.app.fsp.models.forms.FspCategoryForm;
import com.dflex.emkopo.app.fsp.models.forms.FspForm;
import com.dflex.emkopo.app.fsp.services.FinancialServiceProviderService;
import com.dflex.emkopo.commons.models.DataResponse;
import com.dflex.emkopo.commons.models.Response;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/fsp")
public class FinancialServiceProviderController {
    private final FinancialServiceProviderService fspService;

    @Autowired
    public FinancialServiceProviderController(FinancialServiceProviderService fspService) {
        this.fspService = fspService;
    }

    @PostMapping
    public ResponseEntity<DataResponse<?>> create(@Validated @RequestBody FspForm fspForm, HttpServletRequest request) throws URISyntaxException {
        FinancialServiceProvider fsp = fspService.create(fspForm);
        String path = request.getRequestURI();
        return ResponseEntity.created(new URI(path+"/"+fsp.getFspUid())).body(new DataResponse<>(
                HttpStatus.CREATED, "Financial service provider has been created.", path, fsp
        ));
    }

    @GetMapping
    public ResponseEntity<DataResponse<List<FinancialServiceProvider>>> list(HttpServletRequest request){
        return ResponseEntity.ok(new DataResponse<>(
                HttpStatus.OK, "Financial service provider have been retrieved.",
                request.getRequestURI(), fspService.findAll()
        ));
    }

    @GetMapping("{fspId}")
    public ResponseEntity<DataResponse<FinancialServiceProvider>> show(@PathVariable Long fspId, HttpServletRequest request){
        return ResponseEntity.ok(new DataResponse<>(
                HttpStatus.OK, "Financial service provider has been retrieved.",
                request.getRequestURI(), fspService.findById(fspId)
        ));
    }

    @PutMapping("{fspId}")
    public ResponseEntity<DataResponse<FinancialServiceProvider>> update(
            @PathVariable Long fspId, @Validated @RequestBody FspForm fspForm, HttpServletRequest request
    ) throws URISyntaxException {
        String path = request.getRequestURI();
        FinancialServiceProvider fsp = fspService.update(fspId, fspForm);
        return ResponseEntity.ok(new DataResponse<>(
                HttpStatus.OK, "Financial Service Provider has been updated.", path, fsp
        ));
    }
}
