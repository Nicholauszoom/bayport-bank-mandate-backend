package com.dflex.emkopo.app.fsp.repositories;

import java.util.List;
import java.util.Optional;
import com.dflex.emkopo.app.fsp.models.entities.FinancialServiceProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FinancialServiceProviderRepository extends JpaRepository<FinancialServiceProvider, Long> {
	
	FinancialServiceProvider findByFspCodeAndRecordStatusId(String fspCode, Long recordStatusId);

	@Query("from FinancialServiceProvider fsp where fsp.financialServiceProviderCategory.id =:financialServiceProviderCategoryId ")
	List<FinancialServiceProvider> findByFspCategoryId(Long financialServiceProviderCategoryId);

	Optional<FinancialServiceProvider> findByFspCode(String fspCode);

    FinancialServiceProvider findFspByFspReferenceNumber(String fspReferenceNumber);

//    FinancialServiceProvider getFirst();
//	@Query(nativeQuery = true, value = "SELECT COUNT(*) FROM tab_fsp")
//	int count();
}
