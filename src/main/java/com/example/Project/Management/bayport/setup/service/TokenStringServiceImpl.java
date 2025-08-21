package com.example.Project.Management.bayport.setup.service;

import com.example.Project.Management.bayport.setup.repository.TokenStringRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



/**
 * 
 * @author Augustino Mwageni
 *
 */
@Service
public class TokenStringServiceImpl implements TokenStringService {
	
	@Autowired
	private TokenStringRepositoryCustom tokenStringRepository;

	@Override
	public Long getTokenStringByCategory(String tokenCategory) {
		return tokenStringRepository.getTokenStringByCategory(tokenCategory);
	}

}
