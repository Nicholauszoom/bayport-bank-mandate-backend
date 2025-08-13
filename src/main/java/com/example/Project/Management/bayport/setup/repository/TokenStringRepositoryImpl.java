package com.dflex.emkopo.app.setup.repository;

import java.util.ArrayList;
import java.util.List;


import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

/**
 * 
 * @author Augustino Mwageni
 *
 */
@Repository
public class TokenStringRepositoryImpl implements TokenStringRepositoryCustom {

	@PersistenceContext
    EntityManager entityManager;
	
	@SuppressWarnings("unchecked")
	@Override
	public Long getTokenStringByCategory(String tokenCategory) {
		String sqlQuery = "";
		Query q = null;
		List<Object> results = new ArrayList<Object>();
		
		try {
			sqlQuery = "select nextval('"+ tokenCategory +"') ";
			q = entityManager.createNativeQuery(sqlQuery);
			results = q.getResultList();
						
		}catch(Exception e){
			e.printStackTrace();
		}
		
		if(results.isEmpty() || results.get(0) == null){
			return null;
		}

		return Long.parseLong(results.get(0).toString());
	}

}
