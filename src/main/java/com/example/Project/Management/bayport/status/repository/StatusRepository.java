package com.dflex.emkopo.app.status.repository;

import com.dflex.emkopo.app.status.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatusRepository extends JpaRepository<Status, Long> {

   Status findByName(String name);
}
