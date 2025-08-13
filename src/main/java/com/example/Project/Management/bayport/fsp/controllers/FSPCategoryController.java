package com.dflex.emkopo.app.fsp.controllers;

import com.dflex.emkopo.app.fsp.models.entities.FinancialServiceProviderCategory;
import com.dflex.emkopo.app.fsp.models.forms.FspCategoryForm;
import com.dflex.emkopo.app.fsp.services.FSPCategoryService;
import com.dflex.emkopo.commons.models.DataResponse;
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
@RequestMapping("fsp-categories")
public class FSPCategoryController {
    private final FSPCategoryService fspCategoryService;

    @Autowired
    public FSPCategoryController(FSPCategoryService fspCategoryService) {
        this.fspCategoryService = fspCategoryService;
    }

    @PostMapping
    public ResponseEntity<DataResponse<FinancialServiceProviderCategory>> create(
            @Validated @RequestBody FspCategoryForm fspCategoryForm, HttpServletRequest request
    ) throws URISyntaxException {
        String path = request.getRequestURI();
        FinancialServiceProviderCategory fspCategory = fspCategoryService.create(fspCategoryForm);
        return ResponseEntity.created(new URI(path+"/"+fspCategory.getFspUid())).body(new DataResponse<>(
                HttpStatus.CREATED, "Financial service provider category has been created.", path, fspCategory
        ));
    }

    @GetMapping
    public ResponseEntity<DataResponse<List<FinancialServiceProviderCategory>>> list(HttpServletRequest request){

        return ResponseEntity.ok(new DataResponse<>(
                HttpStatus.OK, "Financial service provider categories have been retrieved.",
                request.getRequestURI(), fspCategoryService.getAll()
        ));
    }

    @GetMapping("{categoryId}")
    public ResponseEntity<DataResponse<FinancialServiceProviderCategory>> show(@PathVariable Long categoryId, HttpServletRequest request){
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

    @DeleteMapping("trash/{categoryId}")
    public ResponseEntity<DataResponse<FinancialServiceProviderCategory>> trash(@PathVariable Long categoryId, HttpServletRequest request){
        String path = request.getRequestURI();
        FinancialServiceProviderCategory fspCategory = fspCategoryService.softDelete(categoryId);
        return ResponseEntity.ok(new DataResponse<>(
                HttpStatus.OK, "Financial Service Provider Category has been trashed.", path, fspCategory
        ));
    }

    @DeleteMapping("{categoryId}")
    public ResponseEntity<?> destroy(@PathVariable Long categoryId){
        fspCategoryService.hardDelete(categoryId);
        return ResponseEntity.noContent().build();
    }

}
