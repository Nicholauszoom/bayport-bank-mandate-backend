package com.dflex.emkopo.app.balance.takeover.repository;

import com.dflex.emkopo.app.balance.takeover.entity.Takeoverbal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TakeoverBalanceRepository extends JpaRepository<Takeoverbal, Long> {
}
