package com.dflex.emkopo.app.fsp.services;

import java.util.List;
import java.util.Optional;

import com.dflex.emkopo.app.fsp.models.entities.FinancialServiceProvider;
import com.dflex.emkopo.app.fsp.models.entities.FinancialServiceProviderCategory;
import com.dflex.emkopo.app.fsp.models.forms.FspCategoryForm;
import com.dflex.emkopo.app.fsp.models.forms.FspForm;
import com.dflex.emkopo.app.fsp.repositories.FinancialServiceProviderRepository;
import com.dflex.emkopo.exceptions.NotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
	public FinancialServiceProvider create(FspForm fspForm) {
		FinancialServiceProvider fsp = new FinancialServiceProvider();
		fsp.setFspCode(fspForm.getCode());
		fsp.setFspName(fspForm.getName());
		fsp.setFspShortName(fspForm.getShortName());
		fsp.setFspReferenceNumber(fspForm.getReferenceNumber());


		return fsp;
	}

	@Override
	public FinancialServiceProvider findById(Long id) {
		return financialServiceProviderRepository.findById(id).orElseThrow(
				() -> new NotFoundException("No financial service provider exist by id {}"+id)
		);
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

	@Override
	public FinancialServiceProvider update(Long fspId, FspForm fspForm) {
		FinancialServiceProvider fsp = findById(fspId);
		fsp.setFspCode(fspForm.getCode());
		fsp.setFspName(fspForm.getName());
		fsp.setFspShortName(fspForm.getShortName());
		fsp.setFspReferenceNumber(fspForm.getReferenceNumber());

		financialServiceProviderRepository.save(fsp);
		return fsp;
	}

}