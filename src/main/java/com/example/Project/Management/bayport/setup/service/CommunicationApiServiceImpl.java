package com.dflex.emkopo.app.setup.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dflex.emkopo.app.setup.dto.CommunicationApiDetailsDto;
import com.dflex.emkopo.app.setup.entity.CommunicationApi;
import com.dflex.emkopo.app.setup.repository.CommunicationApiRepository;



/**
 * 
 * @author Augustino Mwageni
 *
 */

@Service
public class CommunicationApiServiceImpl implements CommunicationApiService {

	@Autowired
	private CommunicationApiRepository  communicationApiRepository;

	@Override
	public Optional<CommunicationApi> findById(Long id) {
		return communicationApiRepository.findById(id);
	}

	@Override
	public List<CommunicationApi> findAll() {
		return communicationApiRepository.findAll();
	}

	@Override
	public CommunicationApi saveCommunicationApi(CommunicationApi communicationApi) {
		return communicationApiRepository.save(communicationApi);
	}

	@Override
	public CommunicationApiDetailsDto findByClientIdClientKeyAndApiCategoryIdAndApiVersionNumber(String clientCode, String clientKey,
			Long apiCategoryId, Long apiVersionNumber) {
		return communicationApiRepository.findByClientIdClientKeyAndApiCategoryIdAndApiVersionNumber(clientCode,clientKey,
				apiCategoryId,apiVersionNumber);
	}

}
