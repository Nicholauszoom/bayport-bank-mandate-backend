package com.dflex.emkopo.app.setup.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dflex.emkopo.app.setup.repository.TokenStringRepositoryCustom;



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
