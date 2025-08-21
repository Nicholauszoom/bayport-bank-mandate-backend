package com.example.Project.Management.bayport.takeover.repository;

import com.example.Project.Management.bayport.takeover.entity.Takeoverbal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TakeoverBalanceRepository extends JpaRepository<Takeoverbal, Long> {
}
