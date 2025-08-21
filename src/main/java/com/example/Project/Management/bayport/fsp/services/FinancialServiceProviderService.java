package com.example.Project.Management.bayport.fsp.services;

import com.example.Project.Management.bayport.fsp.models.entities.FinancialServiceProvider;
import com.example.Project.Management.bayport.fsp.models.forms.FspForm;

import java.util.List;
import java.util.Optional;

/**
 * 
 * @author Augustino Mwageni
 *
 */

public interface FinancialServiceProviderService {

	Optional<FinancialServiceProvider> findById(Long id);

	FinancialServiceProvider findByFspCodeAndRecordStatusId(String fspCode, Long recordStatusId);
	
	List<FinancialServiceProvider> findByFspCategoryId(Long paymentFacilitatorCategoryId);
	
	List<FinancialServiceProvider> findAll();

	Optional<FinancialServiceProvider> findByFspCode(String fspCode);
	long count();
}