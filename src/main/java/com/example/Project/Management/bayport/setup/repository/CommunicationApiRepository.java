package com.example.Project.Management.bayport.setup.repository;

import com.example.Project.Management.bayport.setup.entity.CommunicationApi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Augustino Mwageni
 *
 */
@Transactional
public interface CommunicationApiRepository extends JpaRepository<CommunicationApi, Long>, CommunicationApiRepositoryCustom {

}
