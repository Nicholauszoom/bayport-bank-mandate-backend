package com.example.Project.Management.bayport.takeover.service;

import com.example.Project.Management.bayport.takeover.dto.TakeoverBalanceDto;
import com.example.Project.Management.bayport.takeover.entity.Takeoverbalance;
import com.example.Project.Management.bayport.takeover.repository.TakeoverBalanceRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TakeoverBalanceServiceImpl implements TakeoverBalanceService {

    private final TakeoverBalanceRepository takeoverBalanceRepository;

    public TakeoverBalanceServiceImpl(TakeoverBalanceRepository takeoverBalanceRepository) {
        this.takeoverBalanceRepository = takeoverBalanceRepository;
    }

    @Override
    public List<Takeoverbalance> findAll() {
        return takeoverBalanceRepository.findAll();
    }

    @Override
    public List<Takeoverbalance> searchTakeoverBalance(String term) {
        return takeoverBalanceRepository.searchTakeoverBalance(term);
    }

    @Override
    public Optional<Takeoverbalance> findById(long id) {
        return takeoverBalanceRepository.findById(id);
    }
}
