package com.example.Project.Management.bayport.crdb.repository;

import com.example.Project.Management.bayport.crdb.entity.DirectDebitMandate;
import com.example.Project.Management.bayport.crdb.enumerated.MandateRequestType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DirectMandateRepository extends JpaRepository<DirectDebitMandate, Long> {

    @Query("""
    SELECT d FROM DirectDebitMandate d
    WHERE (:term IS NULL OR 
           LOWER(d.phoneNumber) LIKE LOWER(CONCAT('%', :term, '%')) OR
           LOWER(d.debitAccount) LIKE LOWER(CONCAT('%', :term, '%')) OR
           LOWER(d.startDate) LIKE LOWER(CONCAT('%', :term, '%')) OR
           LOWER(d.endDate) LIKE LOWER(CONCAT('%', :term, '%')) OR
           LOWER(d.frequencyType) LIKE LOWER(CONCAT('%', :term, '%')) OR
           FUNCTION('TO_CHAR', d.createdDate, 'YYYY-MM-DD') LIKE CONCAT('%', :term, '%')
    )
    AND (:requestType IS NULL OR d.mandateRequestType = :requestType)
""")
    List<DirectDebitMandate> searchDirectDebitMandate(@Param("term") String term, @Param("requestType") MandateRequestType requestType);

    @Query("SELECT d FROM DirectDebitMandate d WHERE d.mandateRequestType = :requestType ORDER BY d.createdDate DESC")
    List<DirectDebitMandate> findDebitMandateByRequestType(MandateRequestType requestType);
}
