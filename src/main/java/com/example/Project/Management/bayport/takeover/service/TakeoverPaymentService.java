package com.example.Project.Management.bayport.takeover.service;


import com.example.Project.Management.bayport.common.enumerator.RequestType;
import com.example.Project.Management.bayport.takeover.entity.Takeoverpayment;

import java.util.List;
import java.util.Optional;

public interface TakeoverPaymentService {

    void save(Takeoverpayment takeoverpayment);

    List<Takeoverpayment> findAll(RequestType requestType);

    Optional<Takeoverpayment> findById(long id);

    List<Takeoverpayment> searchTakeoverPayment(String term);
}
