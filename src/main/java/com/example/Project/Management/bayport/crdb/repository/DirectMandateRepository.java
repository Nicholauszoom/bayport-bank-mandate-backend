package com.example.Project.Management.bayport.crdb.repository;

import com.example.Project.Management.bayport.crdb.entity.DirectDebitMandate;
import com.example.Project.Management.bayport.crdb.enumerated.MandateRequestType;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DirectMandateRepository extends JpaRepository<DirectDebitMandate, Long> {

    @Query("""
    SELECT d FROM DirectDebitMandate d
    WHERE (:term IS NULL OR 
           LOWER(d.phoneNumber) LIKE LOWER(CONCAT('%', :term, '%')) OR
           LOWER(d.debitAccount) LIKE LOWER(CONCAT('%', :term, '%')) OR
           LOWER(d.frequencyType) LIKE LOWER(CONCAT('%', :term, '%')) OR
           FUNCTION('TO_CHAR', d.startDate, 'YYYY-MM-DD') LIKE CONCAT('%', :term, '%') OR
           FUNCTION('TO_CHAR', d.endDate, 'YYYY-MM-DD') LIKE CONCAT('%', :term, '%') OR
           FUNCTION('TO_CHAR', d.createdDate, 'YYYY-MM-DD') LIKE CONCAT('%', :term, '%')
    )
    AND (:requestType IS NULL OR d.mandateRequestType = :requestType)
""")
    List<DirectDebitMandate> searchDirectDebitMandate(@Param("term") String term, @Param("requestType") MandateRequestType requestType);

    @Query("SELECT d FROM DirectDebitMandate d WHERE d.mandateRequestType = :requestType ORDER BY d.createdDate DESC")
    Page<DirectDebitMandate> findDebitMandateByRequestType(MandateRequestType requestType, Pageable pageable);

    @Query("SELECT d FROM DirectDebitMandate d WHERE d.mandateRequestType = :requestType ORDER BY d.createdDate DESC")
    Page<DirectDebitMandate> findByRequestTypeWithPageableSort(MandateRequestType requestType, Pageable pageable);

    @Query("SELECT d FROM DirectDebitMandate d " +
            "WHERE d.mandateRequestType = :requestType " +
            "AND d.createdDate BETWEEN :startDate AND :endDate " +
            "ORDER BY d.createdDate DESC")
    List<DirectDebitMandate> findDebitMandateByRequestTypeWithStartAndEndDate(
            @Param("requestType") MandateRequestType requestType,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    @Query("SELECT d FROM DirectDebitMandate d WHERE d.mandateRequestType = :requestType ORDER BY d.createdDate DESC")
    List<DirectDebitMandate> findDebitMandatesByRequestTypeNonPageable(MandateRequestType requestType);
}