package com.example.Project.Management.bayport.setup.service;

import com.example.Project.Management.bayport.setup.entity.Currency;
import com.example.Project.Management.bayport.setup.repository.CurrencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * 
 * @author Augustino Mwageni
 *
 */

@Service
public class CurrencyServiceImpl implements CurrencyService{
	
	@Autowired
	private CurrencyRepository currencyRepository;


	@Override
	public List<Currency>getAllCurrency(){
		return currencyRepository.findAll();
	}
	@Override
	public Optional<Currency> findById(Long id) {
		return currencyRepository.findById(id);
	}

	@Override
	public Currency saveCurrency(Currency currency) {
		return currencyRepository.save(currency);
	}

	@Override
	public Currency findByCurrencyCode(String currencyCode) {
		return currencyRepository.findByCurrencyCode(currencyCode);
	}

	@Override
	public boolean deleteCurrencyById(Long id) {
		Optional<Currency> currencyOptional = currencyRepository.findById(id);
		if (currencyOptional.isPresent()){
			currencyRepository.deleteById(id);
			return true;
		}
		return false;
	}
}