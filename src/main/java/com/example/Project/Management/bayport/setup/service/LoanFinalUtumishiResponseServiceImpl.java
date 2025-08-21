package com.example.Project.Management.bayport.setup.service;

import com.example.Project.Management.bayport.loan.entity.LoanFinalUtumishiResponse;
import com.example.Project.Management.bayport.setup.repository.LoanFianalUtumishiResponseRepository;

public class LoanFinalUtumishiResponseServiceImpl implements LoanFinalUtumishiResponseService{

    LoanFianalUtumishiResponseRepository loanFianalUtumishiResponseRepository;

    @Override
    public LoanFinalUtumishiResponse saveloanFinalUtumishiResponse(LoanFinalUtumishiResponse loanFinalUtumishiResponse) {
        return loanFianalUtumishiResponseRepository.save(loanFinalUtumishiResponse);
    }
}
