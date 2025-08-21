package com.example.Project.Management.bayport.setup.service;

import com.example.Project.Management.bayport.setup.dto.CommunicationApiDetailsDto;
import com.example.Project.Management.bayport.setup.entity.CommunicationApi;

import java.util.List;
import java.util.Optional;


/**
 * 
 * @author Augustino Mwageni
 *
 */

public interface CommunicationApiService {

	public Optional<CommunicationApi> findById(Long id);

	public List<CommunicationApi> findAll();
	
	public CommunicationApi saveCommunicationApi(CommunicationApi communicationApi);
	
	public CommunicationApiDetailsDto findByClientIdClientKeyAndApiCategoryIdAndApiVersionNumber(String clientCode, String clientKey,
																								 Long apiCategoryId, Long apiVersionNumber);

}
