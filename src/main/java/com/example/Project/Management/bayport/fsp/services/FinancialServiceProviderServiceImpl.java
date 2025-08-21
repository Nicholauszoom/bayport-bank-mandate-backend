package com.example.Project.Management.bayport.fsp.services;

import com.example.Project.Management.bayport.fsp.models.entities.FinancialServiceProvider;
import com.example.Project.Management.bayport.fsp.models.forms.FspForm;
import com.example.Project.Management.bayport.fsp.repositories.FinancialServiceProviderRepository;
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
public class FinancialServiceProviderServiceImpl implements FinancialServiceProviderService {
	
	@Autowired
	private FinancialServiceProviderRepository financialServiceProviderRepository;


	@Override
	public Optional<FinancialServiceProvider> findById(Long id) {
		return financialServiceProviderRepository.findById(id);
	}

	@Override
	public FinancialServiceProvider findByFspCodeAndRecordStatusId(String fspCode, Long recordStatusId) {
		return financialServiceProviderRepository.findByFspCodeAndRecordStatusId(fspCode, recordStatusId);
	}

	@Override
	public List<FinancialServiceProvider> findByFspCategoryId(Long paymentFacilitatorCategoryId) {
		return financialServiceProviderRepository.findByFspCategoryId(paymentFacilitatorCategoryId);
	}

	@Override
	public List<FinancialServiceProvider> findAll() {
		return financialServiceProviderRepository.findAll();
	}

	@Override
	public Optional<FinancialServiceProvider> findByFspCode(String fspCode) {
		return financialServiceProviderRepository.findByFspCode(fspCode);
	}

	@Override
	public long count() {
		return financialServiceProviderRepository.count();
	}



}