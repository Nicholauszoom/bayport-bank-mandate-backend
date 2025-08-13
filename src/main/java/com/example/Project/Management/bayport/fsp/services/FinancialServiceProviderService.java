package com.dflex.emkopo.app.fsp.services;

import com.dflex.emkopo.app.fsp.models.entities.FinancialServiceProvider;
import com.dflex.emkopo.app.fsp.models.forms.FspCategoryForm;
import com.dflex.emkopo.app.fsp.models.forms.FspForm;

import java.util.List;
import java.util.Optional;

/**
 * 
 * @author Augustino Mwageni
 *
 */

public interface FinancialServiceProviderService {
	/**
	 * Registers a new Financial Service Provider
	 * @param fspForm - Carries fsp registration data
	 * @return FinancialServiceProvider
	 */
	FinancialServiceProvider create(FspForm fspForm);
	
	FinancialServiceProvider findById(Long id);

	FinancialServiceProvider update(Long fspId, FspForm fspForm);
	
	FinancialServiceProvider findByFspCodeAndRecordStatusId(String fspCode, Long recordStatusId);
	
	List<FinancialServiceProvider> findByFspCategoryId(Long paymentFacilitatorCategoryId);
	
	List<FinancialServiceProvider> findAll();

	Optional<FinancialServiceProvider> findByFspCode(String fspCode);
	long count();
}