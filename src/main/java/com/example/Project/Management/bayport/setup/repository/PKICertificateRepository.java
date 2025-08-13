package com.dflex.emkopo.app.setup.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.dflex.emkopo.app.setup.entity.PKICertificate;

/**
 * @author Augustino Mwageni
 *
 */
@Transactional
public interface PKICertificateRepository extends JpaRepository<PKICertificate, Long> {

    
}
