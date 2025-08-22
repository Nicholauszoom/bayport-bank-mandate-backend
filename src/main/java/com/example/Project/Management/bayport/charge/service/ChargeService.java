package com.example.Project.Management.bayport.charge.service;

import com.example.Project.Management.bayport.charge.dto.portal.ChargeDto;
import com.example.Project.Management.bayport.charge.entity.Charge;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface ChargeService {
    ResponseEntity<String> calculateLoanCharges(String message) throws Exception;

    List<Charge> findAll();

    List<ChargeDto> searchCharges(String term);

    Optional<Charge> findById(long id);

    void deleteCharge(long id);

    Charge findLatestChargeByChecknumber(String checkNumber);
}
