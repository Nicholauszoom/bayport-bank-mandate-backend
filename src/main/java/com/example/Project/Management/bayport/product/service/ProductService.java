package com.example.Project.Management.bayport.product.service;

import com.dflex.emkopo.app.product.entity.Product;
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

	ResponseEntity<String> saveProduct(String message) throws Exception;

	
	Product findByProductCode(String productCode);

	List<Product> findByFspCode(String fspCode);

	Product findByProductCodeAndFspCodeAndRecordStatusId(String productCode, String fspCode, Long recordStatusId);

	List<Product> findByFspCodeAndRecordStatusId(String fspCode,Long recordStatusId);

//	Response<Product> registerProductFromPortal(ProductDto productDto);

	ResponseEntity<String> registerProductFromBayPort(String message) throws Exception;

	ResponseEntity<String> saveProductPortalQueue(String message) throws Exception;
}