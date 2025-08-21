package com.example.Project.Management.bayport.status.repository;

import com.example.Project.Management.bayport.status.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatusRepository extends JpaRepository<Status, Long> {

   Status findByName(String name);
}
