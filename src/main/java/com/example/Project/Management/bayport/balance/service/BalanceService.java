package com.example.Project.Management.bayport.balance.service;

import com.example.Project.Management.bayport.balance.dto.portal.BalanceDto;
import com.example.Project.Management.bayport.balance.entity.Balance;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface BalanceService {
    List<Balance> findAll();

    List<BalanceDto> searchBalances(String term);

    Optional<Balance> findById(long id);

    void deleteBalance(long id);
}
