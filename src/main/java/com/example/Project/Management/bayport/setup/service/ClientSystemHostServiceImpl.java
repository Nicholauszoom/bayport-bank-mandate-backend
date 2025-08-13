package com.dflex.emkopo.app.setup.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dflex.emkopo.app.setup.entity.ClientSystemHost;
import com.dflex.emkopo.app.setup.repository.ClientSystemHostRepository;


/**
 * 
 * @author Augustino Mwageni
 *
 */

@Service
public class ClientSystemHostServiceImpl implements ClientSystemHostService {

	@Autowired
	private ClientSystemHostRepository  clientSystemHostRepository;

	@Override
	public Optional<ClientSystemHost> findById(Long id) {
		return clientSystemHostRepository.findById(id);
	}

	@Override
	public List<ClientSystemHost> findAll() {
		return clientSystemHostRepository.findAll();
	}

	@Override
	public ClientSystemHost saveClientSystemHost(ClientSystemHost clientSystemHost) {
		return clientSystemHostRepository.save(clientSystemHost);
	}

	@Override
	public Optional<ClientSystemHost> findByClientKeyAndRecordStatusIdAndIpAddress(String clientKey,
			Long recordStatusId, String ipAddress) {
		return clientSystemHostRepository.findByClientKeyAndRecordStatusIdAndIpAddress(clientKey,
				recordStatusId,ipAddress);
	}

	@Override
	public Boolean existsByClientKeyAndRecordStatusIdAndIpAddress(String clientKey, Long recordStatusId,
			String ipAddress) {
		return clientSystemHostRepository.existsByClientKeyAndRecordStatusIdAndIpAddress(clientKey,recordStatusId,
				ipAddress);
	}

	
}
