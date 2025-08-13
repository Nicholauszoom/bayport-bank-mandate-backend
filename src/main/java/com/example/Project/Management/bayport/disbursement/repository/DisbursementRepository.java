package com.dflex.emkopo.app.disbursement.repository;

import com.dflex.emkopo.app.disbursement.entity.Disbursement;
import com.dflex.emkopo.app.loan.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DisbursementRepository extends JpaRepository<Disbursement, Long> {
    Disbursement findByApplicationNumber(String applicationNumber);
}
