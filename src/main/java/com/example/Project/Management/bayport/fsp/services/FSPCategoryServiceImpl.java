package com.dflex.emkopo.app.fsp.services;

import java.util.List;

import com.dflex.emkopo.app.auth.services.AuthService;
import com.dflex.emkopo.app.fsp.models.entities.FinancialServiceProviderCategory;
import com.dflex.emkopo.app.fsp.models.forms.FspCategoryForm;
import com.dflex.emkopo.app.fsp.repositories.FSPCategoryRepository;
import com.dflex.emkopo.app.users.models.User;
import com.dflex.emkopo.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 
 * @author Augustino Mwageni
 *
 */

@Service
public class FSPCategoryServiceImpl implements FSPCategoryService {
	private final AuthService authService;
	
	@Autowired
	private FSPCategoryRepository fspCategoryRepository;

	@Autowired
    public FSPCategoryServiceImpl(AuthService authService) {
        this.authService = authService;
    }

    @Override
	public FinancialServiceProviderCategory create(FspCategoryForm fspCategoryForm) {
		User user = authService.getUser();
		FinancialServiceProviderCategory fspCategory = new FinancialServiceProviderCategory();
		fspCategory.setCategoryCode(fspCategoryForm.getCode());
		fspCategory.setCategoryName(fspCategoryForm.getName());
		fspCategory.setCreatedByUsername(user.getUsername());

		fspCategoryRepository.save(fspCategory);
		return fspCategory;
	}

	@Override
	public List<FinancialServiceProviderCategory> getAll() {
		return fspCategoryRepository.findAll();
	}

	@Override
	public FinancialServiceProviderCategory getById(Long id) {
		return fspCategoryRepository.findById(id).orElseThrow(() -> new NotFoundException("No financial service provider category exists by id "+id));
	}

	@Override
	public FinancialServiceProviderCategory update(Long fspId, FspCategoryForm fspCategoryForm) {
		FinancialServiceProviderCategory fspCategory = getById(fspId);
		fspCategory.setCategoryCode(fspCategoryForm.getCode());
		fspCategory.setCategoryName(fspCategoryForm.getName());

		fspCategoryRepository.save(fspCategory);
		return fspCategory;
	}

	@Override
	public FinancialServiceProviderCategory softDelete(Long fspId) {
		FinancialServiceProviderCategory fspCategory = getById(fspId);
		fspCategory.setDeleted(true);
		fspCategoryRepository.save(fspCategory);
		return fspCategory;
	}

	@Override
	public void hardDelete(Long fspId) {
		FinancialServiceProviderCategory fspCategory = getById(fspId);
		fspCategoryRepository.delete(fspCategory);
	}
}