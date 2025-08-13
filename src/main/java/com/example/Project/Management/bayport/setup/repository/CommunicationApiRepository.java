package com.dflex.emkopo.app.setup.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.dflex.emkopo.app.setup.entity.CommunicationApi;

/**
 * @author Augustino Mwageni
 *
 */
@Transactional
public interface CommunicationApiRepository extends JpaRepository<CommunicationApi, Long>, CommunicationApiRepositoryCustom {

}
