package com.example.Project.Management.bayport.setup.service;


import com.example.Project.Management.bayport.setup.entity.ApiCategory;

import java.util.List;
import java.util.Optional;



/**
 * 
 * @author Augustino Mwageni
 *
 */

public interface ApiCategoryService {

	public Optional<ApiCategory> findById(Long id);
	
	public List<ApiCategory> findAll();
	
	public ApiCategory createApiCategory(ApiCategory apiCategory);
	
	public ApiCategory updateApiCategory(ApiCategory apiCategory);

}
