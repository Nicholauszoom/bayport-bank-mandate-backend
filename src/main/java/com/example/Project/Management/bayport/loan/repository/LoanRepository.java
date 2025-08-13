package com.example.Project.Management.bayport.loan.repository;

import com.dflex.emkopo.app.loan.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {

    Loan findLoanByLoanNumber(String loanNumber);

    Loan findByLoanNumberAndFspCode(String loanNumber, String fspCode);

    Loan findByLoanNumberAndFsp2Code(String loanNumber, String fsp2Code);

    Optional<Loan> findFirstByApplicationNumber(String loanApplicationNumber);

    @Query("SELECT l FROM Loan l WHERE l.applicationNumber = :loanApplicationNumber")
    Optional<Loan> findByApplicationNumber(@Param("loanApplicationNumber") String loanApplicationNumber);

    List<Loan> findByFspCode(String fspByCode);

//    List<Loan> findByBorrowerAndFspCode(BigInteger borrowerId, String fspByCode);
}
