package com.dflex.emkopo.app.balance.takeover.service;

import com.dflex.emkopo.app.balance.takeover.entity.Takeoverbal;
import com.dflex.emkopo.app.balance.takeover.repository.TakeoverBalanceRepository;
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
