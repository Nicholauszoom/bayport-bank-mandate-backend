package com.example.Project.Management.bayport.setup.repository;

import com.example.Project.Management.bayport.setup.entity.ClientSystemHost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * @author Augustino Mwageni
 *
 */
@Transactional
public interface ClientSystemHostRepository extends JpaRepository<ClientSystemHost, Long> {

	@Query("from ClientSystemHost ch where ch.clientSystem.clientKey =:clientKey and ch.recordStatusId =:recordStatusId and ch.ipAddress =:ipAddress")
	Optional<ClientSystemHost> findByClientKeyAndRecordStatusIdAndIpAddress(String clientKey, Long recordStatusId,
																			String ipAddress);

	@Query("select case when count(ch) > 0 then true else false end  from ClientSystemHost ch where ch.clientSystem.clientKey =:clientKey "
			+ " and ch.recordStatusId =:recordStatusId and ch.ipAddress =:ipAddress")
	Boolean existsByClientKeyAndRecordStatusIdAndIpAddress(String clientKey, Long recordStatusId, String ipAddress);
}
