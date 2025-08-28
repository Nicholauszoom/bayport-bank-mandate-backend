package com.example.Project.Management.bayport.takeover.service;

import com.example.Project.Management.bayport.common.enumerator.RequestType;
import com.example.Project.Management.bayport.takeover.entity.Takeoverpayment;
import com.example.Project.Management.bayport.takeover.repository.TakeoverBalanceRepository;
import com.example.Project.Management.bayport.takeover.repository.TakeoverPaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Service
public class TakeoverPaymentServiceImpl implements TakeoverPaymentService {


    private final TakeoverPaymentRepository takeoverPaymentRepository;

    public TakeoverPaymentServiceImpl(TakeoverBalanceRepository takeoverBalanceRepository, TakeoverPaymentRepository takeoverPaymentRepository) {
        this.takeoverPaymentRepository = takeoverPaymentRepository;
    }

    @Override
    public void save(Takeoverpayment takeoverpayment) {
        takeoverPaymentRepository.save(takeoverpayment);
    }



    @Override
    public List<Takeoverpayment> findAll(RequestType requestType) {
        return takeoverPaymentRepository.findAllByRequestType(requestType);
    }

    @Override
    public Optional<Takeoverpayment> findById(long id) {
        return takeoverPaymentRepository.findById(id);
    }

    @Override
    public List<Takeoverpayment> searchTakeoverPayment(String term) {
        return takeoverPaymentRepository.searchTakeoverPayment(term);
    }
}
