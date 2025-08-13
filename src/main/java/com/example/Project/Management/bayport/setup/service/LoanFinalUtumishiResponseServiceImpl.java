package com.dflex.emkopo.app.setup.service;

import com.dflex.emkopo.app.loan.entity.LoanFinalUtumishiResponse;
import com.dflex.emkopo.app.setup.entity.UtumishiResponse;
import com.dflex.emkopo.app.setup.repository.LoanFianalUtumishiResponseRepository;

public class LoanFinalUtumishiResponseServiceImpl implements LoanFinalUtumishiResponseService{

    LoanFianalUtumishiResponseRepository loanFianalUtumishiResponseRepository;

    @Override
    public LoanFinalUtumishiResponse saveloanFinalUtumishiResponse(LoanFinalUtumishiResponse loanFinalUtumishiResponse) {
        return loanFianalUtumishiResponseRepository.save(loanFinalUtumishiResponse);
    }
}
