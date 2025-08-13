package com.dflex.emkopo.app.setup.repository;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.dflex.emkopo.app.setup.dto.CommunicationApiDetailsDto;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

/**
 * @author Augustino Mwageni
 *
 */
@Repository
@Transactional
public class CommunicationApiRepositoryImpl implements CommunicationApiRepositoryCustom  {
	
	@PersistenceContext
	private EntityManager entityManager;
	

	@Override
	public CommunicationApiDetailsDto findByClientIdClientKeyAndApiCategoryIdAndApiVersionNumber(String clientCode,
			String clientKey, Long apiCategoryId, Long apiVersionNumber) {
		
		CommunicationApiDetailsDto communicationApi = null;
		
		String sqlQuery = "select c.id,c.api_description,c.apply_digital_signature,c.api_url,c.api_url_status_id "
				+ ",c.inbound_status_id,c.outbound_status_id,c.record_status_id apiStatusId, c.api_category_id,c.api_version_number "
				+ ",cs.client_key,cs.record_status_id systemStatusId, a.certificate_alias,a.certificate_passphrase,a.certificate_filename "
				+ ",a.certificate_serial_number,a.record_status_id certificateStatusId,a.certificate_algorithm, sc.client_code system_client_code"
				+ ",sc.record_status_id systemClientStatusId,b.certificate_alias,b.certificate_passphrase,b.certificate_filename,b.certificate_serial_number "
				+ "from tab_communication_api c "
				+ "join tab_client_system cs on c.client_system_id = cs.id "
				+ "join tab_pki_certificate a on cs.pki_certificate_id = a.id "
				+ "join tab_pki_certificate b on a.internal_pki_certificate_id = b.id "
				+ "left join tab_system_client sc on sc.client_system_id = cs.id and sc.client_code =:clientCode "
				+ "where c.api_category_id =:apiCategoryId "
				+ "and c.api_version_number =:apiVersionNumber "
				+ "and cs.client_key =:clientKey ";
		Query q = entityManager.createNativeQuery(sqlQuery);
		q.setParameter("apiCategoryId", apiCategoryId);
		q.setParameter("apiVersionNumber", apiVersionNumber);
		q.setParameter("clientKey", clientKey);
		q.setParameter("clientCode", clientCode);
		
		@SuppressWarnings("unchecked")
		List<Object[]> results = q.getResultList();
		if(results != null && !results.isEmpty()) {
			
			Object[] res = results.get(0);
			
			communicationApi = new CommunicationApiDetailsDto(Long.parseLong(res[0].toString()),
					res[1].toString(),
					Boolean.valueOf(res[2].toString()),
					res[3] != null ? res[3].toString():"",
					Long.parseLong(res[4].toString()),
					
					Long.parseLong(res[5].toString()),
					Long.parseLong(res[6].toString()),
					Long.parseLong(res[7].toString()),
					Long.parseLong(res[8].toString()),
					Long.parseLong(res[9].toString()),
					
					res[10].toString(),
					Long.parseLong(res[11].toString()),
					res[12].toString(),
					res[13].toString(),
					res[14].toString(),
					
					res[15].toString(),
					Long.parseLong(res[16].toString()),
					res[17].toString(),
					res[18] != null ? res[18].toString() : "",
					
					res[19] != null ? Long.parseLong(res[19].toString()):null,
					res[20]==null?"":res[20].toString(),
					res[21]==null?"":res[21].toString(),
					res[22]==null?"":res[22].toString(),
					res[23]==null?"":res[23].toString()
					);
		}
		
		return communicationApi;
	}
}
