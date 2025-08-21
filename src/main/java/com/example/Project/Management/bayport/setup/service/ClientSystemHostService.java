package com.example.Project.Management.bayport.setup.service;

import com.example.Project.Management.bayport.setup.entity.ClientSystemHost;

import java.util.List;
import java.util.Optional;


/**
 * 
 * @author Augustino Mwageni
 *
 */

public interface ClientSystemHostService {

	public Optional<ClientSystemHost> findById(Long id);
	
	public List<ClientSystemHost> findAll();
	
	public ClientSystemHost saveClientSystemHost(ClientSystemHost clientSystemHost);
	
	public Optional<ClientSystemHost> findByClientKeyAndRecordStatusIdAndIpAddress(String clientKey,Long recordStatusId, String ipAddress);

	public Boolean existsByClientKeyAndRecordStatusIdAndIpAddress(String clientKey,Long recordStatusId, String ipAddress);
}
