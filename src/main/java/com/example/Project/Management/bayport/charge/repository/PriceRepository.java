package com.example.Project.Management.bayport.charge.repository;

import com.example.Project.Management.bayport.charge.entity.Price;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PriceRepository extends JpaRepository<Price, Long> {
    @Query("""
    SELECT p FROM Price p
    WHERE p.description = :description
      AND p.loanTerm = (
          SELECT MAX(p2.loanTerm)
          FROM Price p2
          WHERE p2.description = :description
      )
""")
    Price findWithMaxLoanTermAndDescription(@Param("description") String description);


    @Query("""
    SELECT p FROM Price p
    WHERE p.description = :description
      AND p.loanTerm = :tenure
""")
    Price findWithXLoanTermAndDescription(@Param("description") String description, @Param("tenure") int tenure);
}
