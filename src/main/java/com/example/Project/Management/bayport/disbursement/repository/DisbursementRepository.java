package com.example.Project.Management.bayport.disbursement.repository;

import com.example.Project.Management.bayport.disbursement.entity.Disbursement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DisbursementRepository extends JpaRepository<Disbursement, Long> {
    Disbursement findByApplicationNumber(String applicationNumber);
}
