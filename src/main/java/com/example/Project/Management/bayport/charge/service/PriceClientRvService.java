package com.example.Project.Management.bayport.charge.service;

import org.springframework.web.multipart.MultipartFile;

public interface PriceService {
    void savePriceScheduleFromExcel(MultipartFile file) throws Exception;

    void saveClientRvFromExcel(MultipartFile file) throws Exception;
}
