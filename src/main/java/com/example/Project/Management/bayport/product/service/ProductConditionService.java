package com.example.Project.Management.bayport.product.service;

import com.dflex.emkopo.app.product.entity.ProductCondition;

import java.util.List;
import java.util.Optional;

/**
 * 
 * @author Augustino Mwageni
 *
 */

public interface ProductConditionService {
	
	public Optional<ProductCondition> findById(Long id);
	
	public ProductCondition saveProductCondition(ProductCondition productCondition);
	
	public List<ProductCondition> saveProductConditionList(List<ProductCondition> productConditionList);
	
	public ProductCondition findByConditionNumber(String conditionNumber);

	public List<ProductCondition> findByProductIdAndRecordStatusId(Long productId,Long recordStatusId);
}