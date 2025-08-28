package com.example.Project.Management.bayport.balance.service;

import com.example.Project.Management.bayport.balance.dto.portal.BalanceDto;
import com.example.Project.Management.bayport.balance.entity.Balance;
import com.example.Project.Management.bayport.balance.repository.BalanceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class BalanceServiceImpl implements BalanceService {

    private final BalanceRepository balanceRepository;

    public BalanceServiceImpl(BalanceRepository balanceRepository) {
        this.balanceRepository = balanceRepository;
    }

    @Override
    public List<Balance> findAll() {
        return balanceRepository.findAllOrderByCreatedAt();
    }

    @Override
    public List<BalanceDto> searchBalances(String term) {
        return balanceRepository.searchBalances(term);
    }

    @Override
    public Optional<Balance> findById(long id) {
        return balanceRepository.findById(id);
    }

    @Override
    public void deleteBalance(long id) {
        balanceRepository.deleteById(id);
    }
}
