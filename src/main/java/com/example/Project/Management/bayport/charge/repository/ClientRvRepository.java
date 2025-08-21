package com.example.Project.Management.bayport.charge.repository;

import com.example.Project.Management.bayport.charge.entity.ClientRv;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRvRepository extends JpaRepository<ClientRv, Long> {
    ClientRv findByCheckNumber(String checkNumber);
}
