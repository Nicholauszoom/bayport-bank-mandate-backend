package com.example.Project.Management.bayport.charge.service;

import com.example.Project.Management.bayport.charge.entity.ClientRv;
import com.example.Project.Management.bayport.charge.entity.Price;
import org.springframework.web.multipart.MultipartFile;

public interface PriceClientRvService {
    void savePriceScheduleFromExcel(MultipartFile file) throws Exception;

    void saveClientRvFromExcel(MultipartFile file) throws Exception;

    ClientRv findByCheckNumber(String checkNumber);

    Price findWithMaxLoanTermAndDescription(String description);

    Price findWithXLoanTermAndDescription(String description, int tenure);
}
