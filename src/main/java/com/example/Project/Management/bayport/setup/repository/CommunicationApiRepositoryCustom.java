package com.example.Project.Management.bayport.setup.repository;

import com.example.Project.Management.bayport.setup.dto.CommunicationApiDetailsDto;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Augustino Mwageni
 *
 */
@Transactional
public interface CommunicationApiRepositoryCustom {

	public CommunicationApiDetailsDto findByClientIdClientKeyAndApiCategoryIdAndApiVersionNumber(String clientCode, String clientKey,
																								 Long apiCategoryId, Long apiVersionNumber);

}
