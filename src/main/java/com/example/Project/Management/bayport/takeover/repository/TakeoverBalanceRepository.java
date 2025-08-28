package com.example.Project.Management.bayport.takeover.repository;

import com.example.Project.Management.bayport.takeover.dto.TakeoverBalanceDto;
import com.example.Project.Management.bayport.takeover.entity.Takeoverbalance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TakeoverBalanceRepository extends JpaRepository<Takeoverbalance, Long> {

    @Query("SELECT t FROM Takeoverbalance t WHERE t.loanNumber = :loanNumber ORDER BY t.createdDate DESC LIMIT 1")
    Takeoverbalance findLatestBalanceByLoannumber(@Param("loanNumber") String loanNumber);

    @Query("""
    SELECT t FROM Takeoverbalance t
    WHERE (:term IS NULL OR 
           LOWER(t.checkNumber) LIKE LOWER(CONCAT('%', :term, '%')) OR
           LOWER(t.firstName) LIKE LOWER(CONCAT('%', :term, '%')) OR
           LOWER(t.lastName) LIKE LOWER(CONCAT('%', :term, '%')) OR
           LOWER(t.middleName) LIKE LOWER(CONCAT('%', :term, '%')) OR
           LOWER(t.loanNumber) LIKE LOWER(CONCAT('%', :term, '%')) OR
           FUNCTION('TO_CHAR', t.createdDate, 'YYYY-MM-DD') LIKE CONCAT('%', :term, '%')
    )
""")
    List<Takeoverbalance> searchTakeoverBalance(String term);
}
