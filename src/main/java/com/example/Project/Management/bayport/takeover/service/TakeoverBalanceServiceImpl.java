package com.example.Project.Management.bayport.takeover.service;

import com.example.Project.Management.bayport.takeover.entity.Takeoverbal;
import com.example.Project.Management.bayport.takeover.repository.TakeoverBalanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TakeoverBalanceServiceImpl implements TakeoverBalanceService {
    @Autowired
    private TakeoverBalanceRepository takeoverBalanceRepository;

    @Override
    public void save(Takeoverbal takeoverBalance) {
        takeoverBalanceRepository.save(takeoverBalance);
    }
}
