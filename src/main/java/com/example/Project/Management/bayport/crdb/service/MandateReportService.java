package com.example.Project.Management.bayport.crdb.service;

import com.example.Project.Management.bayport.crdb.dto.portal.MandateReportDto;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface MandateReportService {
    Map<String, Object> getMandateReportSummary(LocalDate startDate, LocalDate endDate);

    List<MandateReportDto> getSuccessMandatesReport(LocalDate startDate, LocalDate endDate);

    List<MandateReportDto> getFailedMandatesReport(LocalDate startDate, LocalDate endDate);

    List<MandateReportDto> getAllMandatesReport(LocalDate startDate, LocalDate endDate);

    byte[] exportMandateReport(String reportType, String format, LocalDate startDate, LocalDate endDate);
}
