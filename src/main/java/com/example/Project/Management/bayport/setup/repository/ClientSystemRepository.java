package com.example.Project.Management.bayport.setup.repository;

import com.example.Project.Management.bayport.setup.entity.ClientSystem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Augustino Mwageni
 *
 */
@Transactional
public interface ClientSystemRepository extends JpaRepository<ClientSystem, Long> {

    @Query(value = "select * from tbl_client_system cs where cs.client_type_Id=:clientTypeId", nativeQuery = true)
    public List<ClientSystem> findAllByClientType(@Param("clientTypeId") Long clientTypeId);
    
}
