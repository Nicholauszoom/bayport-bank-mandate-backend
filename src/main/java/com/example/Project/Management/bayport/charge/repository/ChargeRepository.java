package com.example.Project.Management.bayport.charge.repository;

import com.example.Project.Management.bayport.charge.entity.Charge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChargeRepository extends JpaRepository<Charge, Long> {


    @Query("""
    SELECT c FROM Charge c
    WHERE (:term IS NULL OR 
           LOWER(c.checkNumber) LIKE LOWER(CONCAT('%', :term, '%')) OR
           LOWER(c.designationName) LIKE LOWER(CONCAT('%', :term, '%')) OR
           LOWER(c.productCode) LIKE LOWER(CONCAT('%', :term, '%')) OR
           FUNCTION('TO_CHAR', c.createdDate, 'YYYY-MM-DD') LIKE CONCAT('%', :term, '%')
    )
""")
    List<Charge> searchCharges(@Param("term") String term);

    @Query("SELECT c FROM Charge c ORDER BY c.createdDate DESC")
    List<Charge> findAllOrderByCreatedDateDesc();


    @Query("SELECT c FROM Charge c WHERE c.checkNumber = :checkNumber ORDER BY c.createdDate DESC LIMIT 1")
    Charge findLatestChargeByChecknumber(@Param("checkNumber") String checkNumber);
}
