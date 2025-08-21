package com.example.Project.Management.bayport.product.service;

import com.example.Project.Management.bayport.product.entity.ProductCondition;

import java.util.List;
import java.util.Optional;

/**
 * 
 * @author Augustino Mwageni
 *
 */

public interface ProductConditionService {
	
	 Optional<ProductCondition> findById(Long id);
	
	 ProductCondition saveProductCondition(ProductCondition productCondition);
	
	 List<ProductCondition> saveProductConditionList(List<ProductCondition> productConditionList);
	
	 ProductCondition findByConditionNumber(String conditionNumber);

	 List<ProductCondition> findByProductIdAndRecordStatusId(Long productId,Long recordStatusId);
}