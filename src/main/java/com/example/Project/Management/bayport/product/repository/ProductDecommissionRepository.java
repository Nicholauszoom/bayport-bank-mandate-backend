package com.example.Project.Management.bayport.product.repository;

import com.dflex.emkopo.app.product.entity.Product;
import com.dflex.emkopo.app.product.entity.ProductDecommission;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Nicholas Somi
 *
 */

public interface ProductDecommissionRepository extends
        JpaRepository<ProductDecommission, Long> {

    ProductDecommission findByProductId(Product product);

}
