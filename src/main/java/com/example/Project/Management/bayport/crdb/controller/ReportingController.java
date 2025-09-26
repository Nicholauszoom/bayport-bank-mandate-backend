package com.example.Project.Management.bayport.crdb.controller;

import com.example.Project.Management.bayport.crdb.dto.portal.MandateReportDto;
import com.example.Project.Management.bayport.crdb.service.MandateReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.Project.Management.bayport.util.DateFormatter.parseJsDateString;

@Controller
@Slf4j
@RequestMapping("/api/mandate/report")
public class ReportingController {
   private final MandateReportService mandateReportService;

    public ReportingController(MandateReportService mandateReportService) {
        this.mandateReportService = mandateReportService;
    }

    @GetMapping("/summary")
    public ResponseEntity<Map<String, Object>> getMandateReportSummary(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        try {
            Map<String, Object> summary = mandateReportService.getMandateReportSummary(startDate, endDate);
            log.info("Successfully generated mandate report summary");
            return ResponseEntity.ok(summary);
        } catch (Exception e) {
            log.error("Error generating mandate report summary: {}", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new HashMap<>());
        }
    }

    @GetMapping("/success")
    public ResponseEntity<List<MandateReportDto>> getSuccessMandatesReport(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size) {
        try {
            List<MandateReportDto> successMandates = mandateReportService.getSuccessMandatesReport(startDate, endDate);
            log.info("Successfully fetched {} success mandates for report", successMandates.size());
            return ResponseEntity.ok(successMandates);
        } catch (Exception e) {
            log.error("Error fetching success mandates report: {}", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(List.of());
        }
    }

    @GetMapping("/failed")
    public ResponseEntity<List<MandateReportDto>> getFailedMandatesReport(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size) {
        try {
            List<MandateReportDto> failedMandates = mandateReportService.getFailedMandatesReport(startDate, endDate);
            log.info("Successfully fetched {} failed mandates for report", failedMandates.size());
            return ResponseEntity.ok(failedMandates);
        } catch (Exception e) {
            log.error("Error fetching failed mandates report: {}", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(List.of());
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<MandateReportDto>> getAllMandatesReport(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size) {
        try {
            List<MandateReportDto> allMandates = mandateReportService.getAllMandatesReport(startDate, endDate);
            log.info("Successfully fetched {} mandates for report", allMandates.size());
            return ResponseEntity.ok(allMandates);
        } catch (Exception e) {
            log.error("Error fetching all mandates report: {}", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(List.of());
        }
    }

    @GetMapping("/export")
    public ResponseEntity<byte[]> exportMandateReport(
            @RequestParam String reportType,
            @RequestParam String format,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        try {
            LocalDate startDateLocalDate = parseJsDateString(startDate);
            LocalDate endDateLocalDate = parseJsDateString(endDate);

            byte[] reportData = mandateReportService.exportMandateReport(reportType, format, startDateLocalDate, endDateLocalDate);

            String filename = String.format("mandate_report_%s.%s", reportType, format);
            String contentType = getContentType(format);

            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=" + filename)
                    .header("Content-Type", contentType)
                    .body(reportData);
        } catch (Exception e) {
            log.error("Error exporting mandate report: {}", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    private String getContentType(String format) {
        return switch (format.toLowerCase()) {
            case "pdf" -> "application/pdf";
            case "excel" -> "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
            case "csv" -> "text/csv";
            default -> "application/octet-stream";
        };
    }
}
