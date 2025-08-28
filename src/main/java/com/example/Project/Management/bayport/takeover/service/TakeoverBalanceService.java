package com.example.Project.Management.bayport.takeover.service;

import com.example.Project.Management.bayport.takeover.dto.TakeoverBalanceDto;
import com.example.Project.Management.bayport.takeover.entity.Takeoverbalance;

import java.util.List;
import java.util.Optional;

public interface TakeoverBalanceService {
    List<Takeoverbalance> findAll();

    List<Takeoverbalance> searchTakeoverBalance(String term);

    Optional<Takeoverbalance> findById(long id);
}
