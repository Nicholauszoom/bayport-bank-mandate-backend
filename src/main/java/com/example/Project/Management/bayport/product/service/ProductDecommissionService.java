package com.example.Project.Management.bayport.product.service;

import com.example.Project.Management.bayport.product.dto.portal.ProductDto;
import org.springframework.http.ResponseEntity;

/**
 * @author Nicholas Somi
 *
 */

public interface ProductDecommissionService {

    ResponseEntity<?> productDecommissionFromPortal(long id) throws Exception;


}
