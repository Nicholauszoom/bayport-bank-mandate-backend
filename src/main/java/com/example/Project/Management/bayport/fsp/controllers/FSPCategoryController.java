package com.example.Project.Management.bayport.fsp.controllers;

import com.example.Project.Management.bayport.common.entity.DataResponse;
import com.example.Project.Management.bayport.fsp.models.entities.FinancialServiceProviderCategory;
import com.example.Project.Management.bayport.fsp.models.forms.FspCategoryForm;
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
@RequestMapping("fsp-categories")
public class FSPCategoryController {
    private final com.example.Project.Management.bayport.fsp.services.FSPCategoryService fspCategoryService;

    @Autowired
    public FSPCategoryController(com.example.Project.Management.bayport.fsp.services.FSPCategoryService fspCategoryService) {
        this.fspCategoryService = fspCategoryService;
    }

    @GetMapping
    public ResponseEntity<DataResponse<List<FinancialServiceProviderCategory>>> list(HttpServletRequest request){

        return ResponseEntity.ok(new DataResponse<>(
                HttpStatus.OK, "Financial service provider categories have been retrieved.",
                request.getRequestURI(), fspCategoryService.getAll()
        ));
    }

    @GetMapping("{categoryId}")
    public ResponseEntity<DataResponse<Optional<FinancialServiceProviderCategory>>> show(@PathVariable Long categoryId, HttpServletRequest request){
        return ResponseEntity.ok(new DataResponse<>(
                HttpStatus.OK, "Financial service provider category has been retrieved.",
                request.getRequestURI(), fspCategoryService.getById(categoryId)
        ));
    }

    @PutMapping("{categoryId}")
    public ResponseEntity<DataResponse<FinancialServiceProviderCategory>> update(
            @PathVariable Long categoryId, @Validated @RequestBody FspCategoryForm fspCategoryForm, HttpServletRequest request
    ) throws URISyntaxException {
        String path = request.getRequestURI();
        FinancialServiceProviderCategory fspCategory = fspCategoryService.update(categoryId, fspCategoryForm);
        return ResponseEntity.ok(new DataResponse<>(
                HttpStatus.OK, "Financial Service Provider Category has been updated.", path, fspCategory
        ));
    }

    @DeleteMapping("{categoryId}")
    public ResponseEntity<?> destroy(@PathVariable Long categoryId){
        fspCategoryService.hardDelete(categoryId);
        return ResponseEntity.noContent().build();
    }

}
