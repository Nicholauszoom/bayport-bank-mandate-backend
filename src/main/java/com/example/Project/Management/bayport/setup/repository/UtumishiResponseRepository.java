package com.example.Project.Management.bayport.setup.repository;

import com.example.Project.Management.bayport.setup.entity.UtumishiResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UtumishiResponseRepository extends JpaRepository<UtumishiResponse, Long> {

}
