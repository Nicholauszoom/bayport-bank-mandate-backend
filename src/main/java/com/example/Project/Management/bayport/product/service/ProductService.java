package com.example.Project.Management.bayport.product.service;

import com.example.Project.Management.bayport.product.dto.portal.ProductDto;
import com.example.Project.Management.bayport.product.entity.Product;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

/**
 * 
 * @author Augustino Mwageni
 *
 */

public interface ProductService {

	List<Product> getAllProducts();

	Optional<Product> findById(Long id);
	
	Product findByProductCode(String productCode);

	List<Product> findByFspCode(String fspCode);

	Product findByProductCodeAndFspCodeAndRecordStatusId(String productCode, String fspCode, Long recordStatusId);

	List<Product> findByFspCodeAndRecordStatusId(String fspCode,Long recordStatusId);

	ResponseEntity<?> registerProductFromPortal(ProductDto productDto) throws Exception;

    List<Product> findAll();
}