package com.example.Project.Management.bayport.setup.repository;

import com.example.Project.Management.bayport.setup.entity.PKICertificate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Augustino Mwageni
 *
 */
@Transactional
public interface PKICertificateRepository extends JpaRepository<PKICertificate, Long> {

    
}
