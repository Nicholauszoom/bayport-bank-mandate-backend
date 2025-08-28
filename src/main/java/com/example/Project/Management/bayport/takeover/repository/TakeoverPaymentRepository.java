package com.example.Project.Management.bayport.takeover.repository;

import com.example.Project.Management.bayport.common.enumerator.RequestType;
import com.example.Project.Management.bayport.takeover.dto.TakeoverPaymentDto;
import com.example.Project.Management.bayport.takeover.entity.Takeoverpayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TakeoverPaymentRepository extends JpaRepository<Takeoverpayment,Long> {

    @Query("""
    SELECT t FROM Takeoverpayment t
    WHERE (:term IS NULL OR 
           LOWER(t.applicationNumber) LIKE LOWER(CONCAT('%', :term, '%')) OR
           LOWER(t.paymentDate) LIKE LOWER(CONCAT('%', :term, '%')) OR
           LOWER(t.loanNumber) LIKE LOWER(CONCAT('%', :term, '%')) OR
           FUNCTION('TO_CHAR', t.createdDate, 'YYYY-MM-DD') LIKE CONCAT('%', :term, '%')
    )
""")
    List<Takeoverpayment> searchTakeoverPayment(String term);

    @Query("SELECT t FROM Takeoverpayment t WHERE t.requestType = :requestType")
    List<Takeoverpayment> findAllByRequestType(@Param("requestType") RequestType requestType);
}
