package com.example.Project.Management.bayport.product.repository;

import com.example.Project.Management.bayport.product.entity.Product;
import com.example.Project.Management.bayport.product.entity.ProductDecommission;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Nicholas Somi
 *
 */

public interface ProductDecommissionRepository extends
        JpaRepository<ProductDecommission, Long> {

    ProductDecommission findByProductId(Product product);

}
