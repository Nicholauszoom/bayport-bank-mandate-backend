package com.dflex.emkopo.app.setup.controller;

import com.dflex.emkopo.app.product.entity.Product;
import com.dflex.emkopo.app.setup.entity.Currency;
import com.dflex.emkopo.app.setup.repository.CurrencyRepository;
import com.dflex.emkopo.app.setup.service.CurrencyService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class CurrencyController {


    CurrencyService currencyService;

    @Autowired
    void CurrencyController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @PostMapping("/currency")
    public Currency saveCurrency(@RequestBody @Valid Currency currency) {
        return currencyService.saveCurrency(currency);
    }

    @GetMapping("/currency/{id}")
    public ResponseEntity<Currency> findById(@PathVariable("id") Long currencyId) {
        Optional<Currency> currencyOptional = currencyService.findById(currencyId);
        if (currencyOptional.isPresent()) {
            return new ResponseEntity<>(currencyOptional.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    @GetMapping("/currency")
    public ResponseEntity<List<Currency>> getAllCurrency() {
        List<Currency> currency = currencyService.getAllCurrency();
        return new ResponseEntity<>(currency, HttpStatus.OK);
    }

    @GetMapping("/currency/{currencyCode}")
    public ResponseEntity<Currency> findByCurrencyCode (@PathVariable("currencyCode") String currencyCode){
        return new ResponseEntity<>(currencyService.findByCurrencyCode(currencyCode), HttpStatus.OK);
    }

    @DeleteMapping("/currency/{id}")
    public ResponseEntity<String> deleteCurrencyById(@PathVariable("id") Long id){
        boolean isDeleted = currencyService.deleteCurrencyById(id);
        if(isDeleted){
            return new ResponseEntity<>("Currency item deleted successfully.", HttpStatus.OK);
        }else {
            return new ResponseEntity<>("Currency item not found",HttpStatus.NOT_FOUND);
        }

    }

}