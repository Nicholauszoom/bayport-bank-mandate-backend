package com.example.Project.Management.bayport.setup.service;

import com.example.Project.Management.bayport.setup.entity.ApiCategory;
import com.example.Project.Management.bayport.setup.repository.ApiCategoryRepository;
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
public class ApiCategoryServiceImpl implements ApiCategoryService {

	@Autowired
	private ApiCategoryRepository apiCategoryRepository;

	@Override
	public Optional<ApiCategory> findById(Long id) {
		return apiCategoryRepository.findById(id);
	}

	@Override
	public List<ApiCategory> findAll() {
		return apiCategoryRepository.findAll();
	}

	@Override
	public ApiCategory createApiCategory(ApiCategory apiCategory) {
		return apiCategoryRepository.save(apiCategory);
	}

	@Override
	public ApiCategory updateApiCategory(ApiCategory apiCategory) {
		return apiCategoryRepository.save(apiCategory);
	}

}
