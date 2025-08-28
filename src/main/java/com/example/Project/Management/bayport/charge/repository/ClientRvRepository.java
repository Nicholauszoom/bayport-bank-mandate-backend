package com.example.Project.Management.bayport.charge.repository;

import com.example.Project.Management.bayport.charge.entity.ClientRv;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRvRepository extends JpaRepository<ClientRv, Long> {
    ClientRv findByCheckNumber(String checkNumber);

    @Query("SELECT c FROM ClientRv c WHERE c.checkNumber = :checkNumber")
    Optional<ClientRv> findByCheckNumbers(@Param("checkNumber") String checkNumber);

}
