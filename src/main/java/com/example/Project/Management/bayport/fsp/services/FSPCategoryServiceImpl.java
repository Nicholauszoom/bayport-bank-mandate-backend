package com.example.Project.Management.bayport.fsp.services;

import com.example.Project.Management.bayport.fsp.models.entities.FinancialServiceProviderCategory;
import com.example.Project.Management.bayport.fsp.models.forms.FspCategoryForm;
import com.example.Project.Management.bayport.fsp.repositories.FSPCategoryRepository;
import com.example.Project.Management.bayport.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * 
 * @author Augustino Mwageni
 *
 */

@Service
public class FSPCategoryServiceImpl implements FSPCategoryService {
	private final AuthenticationService authService;
	
	@Autowired
	private FSPCategoryRepository fspCategoryRepository;

	@Autowired
    public FSPCategoryServiceImpl(AuthenticationService authService) {
        this.authService = authService;
    }

    @Override
	public FinancialServiceProviderCategory create(FspCategoryForm fspCategoryForm) {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();

		FinancialServiceProviderCategory fspCategory = new FinancialServiceProviderCategory();
		fspCategory.setCategoryCode(fspCategoryForm.getCode());
		fspCategory.setCategoryName(fspCategoryForm.getName());
		fspCategoryRepository.save(fspCategory);
		return fspCategory;
	}

	@Override
	public List<FinancialServiceProviderCategory> getAll() {
		return fspCategoryRepository.findAll();
	}

	@Override
	public Optional<FinancialServiceProviderCategory> getById(Long id) {
		return fspCategoryRepository.findById(id);
	}

	@Override
	public FinancialServiceProviderCategory update(Long fspId, FspCategoryForm fspCategoryForm) {
		Optional<FinancialServiceProviderCategory> fspCategory = getById(fspId);
		fspCategory.get().setCategoryCode(fspCategoryForm.getCode());
		fspCategory.get().setCategoryName(fspCategoryForm.getName());

		fspCategoryRepository.save(fspCategory.get());
		return fspCategory.get();
	}
	

	@Override
	public void hardDelete(Long fspId) {
		Optional<FinancialServiceProviderCategory> fspCategory = getById(fspId);
		fspCategoryRepository.delete(fspCategory.get());
	}
}