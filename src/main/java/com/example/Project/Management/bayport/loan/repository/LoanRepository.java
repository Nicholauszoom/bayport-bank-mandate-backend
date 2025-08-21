package com.example.Project.Management.bayport.loan.repository;

import com.example.Project.Management.bayport.charge.entity.Charge;
import com.example.Project.Management.bayport.common.enumerator.RequestType;
import com.example.Project.Management.bayport.loan.dto.portal.LoanDto;
import com.example.Project.Management.bayport.loan.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {

    Optional<Loan> findFirstByApplicationNumber(String loanApplicationNumber);

    @Query("SELECT l FROM Loan l WHERE l.applicationNumber = :loanApplicationNumber")
    Optional<Loan> findByApplicationNumber(@Param("loanApplicationNumber") String loanApplicationNumber);


    @Query("""
    SELECT l FROM Loan l
    WHERE (:term IS NULL OR 
           LOWER(l.checkNumber) LIKE LOWER(CONCAT('%', :term, '%')) OR
           LOWER(l.applicationNumber) LIKE LOWER(CONCAT('%', :term, '%')) OR
           LOWER(l.firstName) LIKE LOWER(CONCAT('%', :term, '%')) OR
           LOWER(l.lastName) LIKE LOWER(CONCAT('%', :term, '%')) OR
           LOWER(l.middleName) LIKE LOWER(CONCAT('%', :term, '%')) OR
           LOWER(l.loanType) LIKE LOWER(CONCAT('%', :term, '%')) OR
           LOWER(l.requestType) LIKE LOWER(CONCAT('%', :term, '%')) OR
           FUNCTION('TO_CHAR', l.createdDate, 'YYYY-MM-DD') LIKE CONCAT('%', :term, '%')
    )
    AND (:requestType IS NULL OR l.requestType = :requestType)
""")
    List<Loan> searchLoans(@Param("term") String term, @Param("requestType") RequestType requestType);



    @Query("SELECT l FROM Loan l WHERE l.requestType = :requestType")
    List<Loan> findLoanByRequestType(@Param("requestType") RequestType requestType);

}
