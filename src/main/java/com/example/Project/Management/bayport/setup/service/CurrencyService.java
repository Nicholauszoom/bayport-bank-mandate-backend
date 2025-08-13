package com.dflex.emkopo.app.setup.service;

import com.dflex.emkopo.app.setup.entity.Currency;

import java.util.List;
import java.util.Optional;

/**
 * 
 * @author Augustino Mwageni
 *
 */

public interface CurrencyService {

    List<Currency> getAllCurrency();

    Optional<Currency> findById(Long id);
	
	Currency saveCurrency(Currency currency);
	
	Currency findByCurrencyCode(String currencyCode);

    boolean deleteCurrencyById(Long id);
}