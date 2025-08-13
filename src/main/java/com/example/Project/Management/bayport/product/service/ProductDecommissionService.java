package com.example.Project.Management.bayport.product.service;

import org.springframework.http.ResponseEntity;

/**
 * @author Nicholas Somi
 *
 */

public interface ProductDecommissionService {

    ResponseEntity<String> productDecommission(String message) throws Exception;

    ResponseEntity<String> productDecommissionPortalQueue(String message) throws Exception;


}
