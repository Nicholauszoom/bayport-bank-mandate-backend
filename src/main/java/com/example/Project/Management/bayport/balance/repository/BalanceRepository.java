package com.example.Project.Management.bayport.balance.repository;

import com.example.Project.Management.bayport.balance.dto.portal.BalanceDto;
import com.example.Project.Management.bayport.balance.entity.Balance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BalanceRepository extends JpaRepository<Balance, Long> {

    @Query("SELECT b FROM Balance b ORDER BY b.createdDate DESC")
    List<Balance> findAllOrderByCreatedAt();

    @Query("""
    SELECT b FROM Balance b
    WHERE (:term IS NULL OR 
           LOWER(b.checkNumber) LIKE LOWER(CONCAT('%', :term, '%')) OR
           LOWER(b.firstName) LIKE LOWER(CONCAT('%', :term, '%')) OR
           LOWER(b.middleName) LIKE LOWER(CONCAT('%', :term, '%')) OR
           LOWER(b.lastName) LIKE LOWER(CONCAT('%', :term, '%')) OR
           FUNCTION('TO_CHAR', b.createdDate, 'YYYY-MM-DD') LIKE CONCAT('%', :term, '%')
    )
""")
    List<BalanceDto> searchBalances(String term);

}
