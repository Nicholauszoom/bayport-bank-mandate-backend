package com.example.Project.Management.bayport.crdb.service;

import com.example.Project.Management.bayport.crdb.dto.portal.MandateReportDto;
import com.example.Project.Management.bayport.crdb.entity.DirectDebitMandate;
import com.example.Project.Management.bayport.crdb.enumerated.MandateRequestType;
import com.example.Project.Management.bayport.crdb.repository.DirectMandateRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.example.Project.Management.bayport.util.DateFormatter.convertToLocalDate;

@Service
@Slf4j
public class MandateReportServiceImpl implements MandateReportService {

    private final DirectMandateRepository directMandateRepository;

    public MandateReportServiceImpl(DirectMandateRepository directMandateRepository) {
        this.directMandateRepository = directMandateRepository;
    }

    @Override
    public Map<String, Object> getMandateReportSummary(LocalDate startDate, LocalDate endDate) {
        try {
            List<DirectDebitMandate> mandates = filterByDateRange(getAllMandates(), startDate, endDate);

            long totalMandates = mandates.size();
            long successMandates = mandates.stream()
                    .filter(this::isSuccessMandate)
                    .count();
            long failedMandates = mandates.stream()
                    .filter(this::isFailedMandate)
                    .count();
            long pendingMandates = totalMandates - successMandates - failedMandates;

            BigDecimal totalAmount = mandates.stream()
                    .map(DirectDebitMandate::getTotalAmount)
                    .filter(Objects::nonNull)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal successAmount = mandates.stream()
                    .filter(this::isSuccessMandate)
                    .map(DirectDebitMandate::getTotalAmount)
                    .filter(Objects::nonNull)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            Map<String, Object> summary = new HashMap<>();
            summary.put("totalMandates", totalMandates);
            summary.put("successMandates", successMandates);
            summary.put("failedMandates", failedMandates);
            summary.put("pendingMandates", pendingMandates);
            summary.put("successRate", totalMandates > 0 ? (double) successMandates / totalMandates * 100 : 0);
            summary.put("totalAmount", totalAmount);
            summary.put("successAmount", successAmount);
            summary.put("reportDate", LocalDateTime.now());

            return summary;
        } catch (Exception e) {
            log.error("Error generating mandate report summary: {}", e);
            throw new RuntimeException("Failed to generate report summary: {}", e);
        }
    }

    private List<DirectDebitMandate> getAllMandates() {
        return directMandateRepository.findAll();
    }

    @Override
    public List<MandateReportDto> getSuccessMandatesReport(LocalDate startDate, LocalDate endDate) {
        List<DirectDebitMandate> mandates = directMandateRepository.findDebitMandateByRequestType(MandateRequestType.SUBMITTED);
        return mandates.stream()
                .filter(this::isSuccessMandate)
                .map(this::convertToReportDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<MandateReportDto> getFailedMandatesReport(LocalDate startDate, LocalDate endDate) {
        List<DirectDebitMandate> mandates = filterByDateRange(getAllMandates(), startDate, endDate);
        return mandates.stream()
                .filter(this::isFailedMandate)
                .map(this::convertToReportDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<MandateReportDto> getAllMandatesReport(LocalDate startDate, LocalDate endDate) {
        List<DirectDebitMandate> mandates = filterByDateRange(getAllMandates(), startDate, endDate);
        return mandates.stream()
                .map(this::convertToReportDto)
                .collect(Collectors.toList());
    }

    @Override
    public byte[] exportMandateReport(String reportType, String format, LocalDate startDate, LocalDate endDate) {
        log.info("PassHere*********************");
        List<MandateReportDto> reportData;

        switch (reportType.toLowerCase()) {
            case "success":
                reportData = getSuccessMandatesReport(startDate, endDate);
                break;
            case "failed":
                reportData = getFailedMandatesReport(startDate, endDate);
                break;
            case "all":
            default:
                reportData = getAllMandatesReport(startDate, endDate);
                break;
        }

        switch (format.toLowerCase()) {
            case "pdf":
                return generatePdfReport(reportData, reportType);
            case "excel":
                return generateExcelReport(reportData, reportType);
            case "csv":
                return generateCsvReport(reportData, reportType);
            default:
                throw new IllegalArgumentException("Unsupported format: " + format);
        }
    }


    private boolean isSuccessMandate(DirectDebitMandate mandate) {
        return mandate.getMandateRequestType() != null && mandate.getMandateRequestType().equals(MandateRequestType.SUBMITTED);
    }

    private boolean isFailedMandate(DirectDebitMandate mandate) {
        return mandate.getMandateRequestType() != null && mandate.getMandateRequestType().equals(MandateRequestType.FAILED);
    }

    private MandateReportDto convertToReportDto(DirectDebitMandate mandate) {
        String status = "PENDING";
        if (isSuccessMandate(mandate)) {
            status = "SUCCESS";
        } else if (isFailedMandate(mandate)) {
            status = "FAILED";
        }

        return MandateReportDto.builder()
                .id(mandate.getId())
                .partnerId(mandate.getPartnerId())
                .phoneNumber(mandate.getPhoneNumber())
                .debitAccount(mandate.getDebitAccount())
                .totalAmount(mandate.getTotalAmount())
                .installmentAmount(mandate.getInstallmentAmount())
                .startDate(mandate.getStartDate())
                .endDate(mandate.getEndDate())
                .frequencyType(mandate.getFrequencyType())
                .frequency(Integer.valueOf(mandate.getFrequency()))
                .billDetails(mandate.getBillDetails())
                .mandateRequestType(mandate.getMandateRequestType())
                .statusCode(mandate.getStatusCode())
                .responseDescription(mandate.getResponseDescription())
                .errorCode(mandate.getErrorCode())
                .errorMessage(mandate.getErrorMessage())
                .submittedDate(mandate.getSubmittedDate())
                .createdDate(mandate.getCreatedDate())
                .status(status)
                .failureReason(mandate.getErrorMessage())
                .build();
    }

    private List<DirectDebitMandate> filterByDateRange(List<DirectDebitMandate> mandates, LocalDate startDate, LocalDate endDate) {
        return mandates.stream()
                .filter(mandate -> {
                    LocalDate createdDate = mandate.getCreatedDate() != null ?
                            convertToLocalDate(mandate.getCreatedDate()) : null;

                    if (startDate != null && createdDate != null && createdDate.isBefore(startDate)) {
                        return false;
                    }
                    if (endDate != null && createdDate != null && createdDate.isAfter(endDate)) {
                        return false;
                    }
                    return true;
                })
                .collect(Collectors.toList());
    }

    // Helper methods for report generation
    private byte[] generatePdfReport(List<MandateReportDto> data, String reportType) {
        // Implement PDF generation using iText or similar library
        // This is a placeholder - you'll need to implement actual PDF generation
        return new byte[0];
    }

    private byte[] generateExcelReport(List<MandateReportDto> data, String reportType) {
        // Implement Excel generation using Apache POI
        // This is a placeholder - you'll need to implement actual Excel generation
        return new byte[0];
    }

    private byte[] generateCsvReport(List<MandateReportDto> data, String reportType) {
        // Implement CSV generation
        // This is a placeholder - you'll need to implement actual CSV generation
        return new byte[0];
    }
}
