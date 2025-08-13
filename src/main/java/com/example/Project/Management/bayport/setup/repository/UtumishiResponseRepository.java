package com.dflex.emkopo.app.setup.repository;

import com.dflex.emkopo.app.setup.entity.UtumishiResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UtumishiResponseRepository extends JpaRepository<UtumishiResponse, Long> {

}
