package com.example.Project.Management.bayport.crdb.service;

import com.example.Project.Management.bayport.crdb.dto.portal.MandateReportDto;
import com.example.Project.Management.bayport.crdb.entity.DirectDebitMandate;
import com.example.Project.Management.bayport.crdb.enumerated.MandateRequestType;
import com.example.Project.Management.bayport.crdb.repository.DirectMandateRepository;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

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
            log.error("Error generating mandate report summary: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to generate report summary", e);
        }
    }

    private List<DirectDebitMandate> getAllMandates() {
        return directMandateRepository.findAll();
    }

    @Override
    public List<MandateReportDto> getSuccessMandatesReport(LocalDate startDate, LocalDate endDate) {
        List<DirectDebitMandate> mandates = directMandateRepository.findDebitMandatesByRequestTypeNonPageable(MandateRequestType.SUBMITTED);
        return filterByDateRange(mandates, startDate, endDate).stream()
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
        log.info("Generating {} report in {} format for date range: {} to {}",
                reportType, format, startDate, endDate);
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

        try {
            switch (format.toLowerCase()) {
                case "pdf":
                    return generatePdfReport(reportData, reportType);
                case "xlsx":
                    return generateExcelReport(reportData, reportType);
                case "csv":
                    return generateCsvReport(reportData, reportType);
                default:
                    throw new IllegalArgumentException("Unsupported format: " + format);
            }
        } catch (Exception e) {
            log.error("Error generating {} report: {}", format, e.getMessage(), e);
            throw new RuntimeException("Failed to generate " + format + " report", e);
        }
    }

    private boolean isSuccessMandate(DirectDebitMandate mandate) {
        return mandate.getMandateRequestType() != null &&
                mandate.getMandateRequestType().equals(MandateRequestType.SUBMITTED);
    }

    private boolean isFailedMandate(DirectDebitMandate mandate) {
        return mandate.getMandateRequestType() != null &&
                mandate.getMandateRequestType().equals(MandateRequestType.FAILED);
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

    private List<DirectDebitMandate> filterByDateRange(List<DirectDebitMandate> mandates,
                                                       LocalDate startDate, LocalDate endDate) {
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

    // PDF Report Generation using iText
    private byte[] generatePdfReport(List<MandateReportDto> data, String reportType) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            // Initialize PDF writer and document
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);

            // Set up fonts
            PdfFont boldFont = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
            PdfFont regularFont = PdfFontFactory.createFont(StandardFonts.HELVETICA);

            // Add title
            Paragraph title = new Paragraph("Direct Debit Mandate Report")
                    .setFont(boldFont)
                    .setFontSize(20)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER);
            document.add(title);

            // Add report type
            Paragraph reportTypeTitle = new Paragraph("Report Type: " + reportType.toUpperCase())
                    .setFont(regularFont)
                    .setFontSize(14)
                    .setTextAlignment(TextAlignment.CENTER);
            document.add(reportTypeTitle);

            // Add generation date
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            Paragraph dateInfo = new Paragraph("Generated on: " + LocalDateTime.now().format(formatter))
                    .setFont(regularFont)
                    .setFontSize(10)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontColor(ColorConstants.GRAY);
            document.add(dateInfo);

            // Add spacing
            document.add(new Paragraph("\n"));

            // Add summary section
            Paragraph summaryTitle = new Paragraph("Summary")
                    .setFont(boldFont)
                    .setFontSize(14)
                    .setBold();
            document.add(summaryTitle);

            BigDecimal totalAmount = data.stream()
                    .map(MandateReportDto::getTotalAmount)
                    .filter(Objects::nonNull)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            long successCount = data.stream()
                    .filter(d -> "SUCCESS".equals(d.getStatus()))
                    .count();

            long failedCount = data.stream()
                    .filter(d -> "FAILED".equals(d.getStatus()))
                    .count();

            Paragraph summary = new Paragraph(
                    "Total Records: " + data.size() +
                            "\nSuccess: " + successCount +
                            "\nFailed: " + failedCount +
                            "\nTotal Amount: " + totalAmount.toString())
                    .setFont(regularFont)
                    .setFontSize(10);
            document.add(summary);
            document.add(new Paragraph("\n"));

            // Add table headers
            String[] headers = {"ID", "Partner ID", "Phone Number", "Account",
                    "Total Amount", "Installment", "Status", "Created Date"};

            // Add footer
            document.add(new Paragraph("\n"));
            Paragraph footer = new Paragraph("End of Report - Total Records: " + data.size())
                    .setFont(regularFont)
                    .setFontSize(10)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontColor(ColorConstants.GRAY);
            document.add(footer);

            // Close document
            document.close();

            return baos.toByteArray();

        } catch (Exception e) {
            log.error("Error generating PDF report: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to generate PDF report", e);
        }
    }
    // Excel Report Generation using Apache POI
    private byte[] generateExcelReport(List<MandateReportDto> data, String reportType) {
        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Mandate Report");

            // Create header style
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setFontHeightInPoints((short) 12);
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            // Create title row
            Row titleRow = sheet.createRow(0);
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellValue("Mandate Report - " + reportType.toUpperCase());
            titleCell.setCellStyle(headerStyle);

            // Create date row
            Row dateRow = sheet.createRow(1);
            Cell dateCell = dateRow.createCell(0);
            dateCell.setCellValue("Generated on: " + LocalDateTime.now().format(DATETIME_FORMATTER));

            // Create header row
            Row headerRow = sheet.createRow(3);
            String[] headers = {"ID", "Partner ID", "Phone Number", "Debit Account",
                    "Total Amount", "Installment Amount", "Start Date", "End Date",
                    "Frequency Type", "Frequency", "Status", "Status Code",
                    "Response Description", "Error Code", "Error Message",
                    "Submitted Date", "Created Date", "Failure Reason"};

            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // Create data rows
            int rowNum = 4;
            for (MandateReportDto dto : data) {
                Row row = sheet.createRow(rowNum++);

                row.createCell(0).setCellValue(dto.getId() != null ? dto.getId() : 0);
                row.createCell(1).setCellValue(dto.getPartnerId() != null ? dto.getPartnerId() : "");
                row.createCell(2).setCellValue(dto.getPhoneNumber() != null ? dto.getPhoneNumber() : "");
                row.createCell(3).setCellValue(dto.getDebitAccount() != null ? dto.getDebitAccount() : "");
                row.createCell(4).setCellValue(dto.getTotalAmount() != null ? dto.getTotalAmount().doubleValue() : 0);
                row.createCell(5).setCellValue(dto.getInstallmentAmount() != null ? dto.getInstallmentAmount().doubleValue() : 0);
                row.createCell(6).setCellValue(dto.getStartDate() != null ? dto.getStartDate().toString() : "");
                row.createCell(7).setCellValue(dto.getEndDate() != null ? dto.getEndDate().toString() : "");
                row.createCell(8).setCellValue(dto.getFrequencyType() != null ? dto.getFrequencyType() : "");
                row.createCell(9).setCellValue(dto.getFrequency() != null ? dto.getFrequency() : 0);
                row.createCell(10).setCellValue(dto.getStatus());
                row.createCell(11).setCellValue(dto.getStatusCode() != null ? dto.getStatusCode() : "");
                row.createCell(12).setCellValue(dto.getResponseDescription() != null ? dto.getResponseDescription() : "");
                row.createCell(13).setCellValue(dto.getErrorCode() != null ? dto.getErrorCode() : "");
                row.createCell(14).setCellValue(dto.getErrorMessage() != null ? dto.getErrorMessage() : "");
                row.createCell(15).setCellValue(dto.getSubmittedDate() != null ? dto.getSubmittedDate().toString() : "");
                row.createCell(16).setCellValue(dto.getCreatedDate() != null ? dto.getCreatedDate().toString() : "");
                row.createCell(17).setCellValue(dto.getFailureReason() != null ? dto.getFailureReason() : "");
            }

            // Auto-size columns
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(baos);
            return baos.toByteArray();
        } catch (IOException e) {
            log.error("Error generating Excel report: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to generate Excel report", e);
        }
    }

    // CSV Report Generation using Apache Commons CSV
    private byte[] generateCsvReport(List<MandateReportDto> data, String reportType) {
        try (StringWriter sw = new StringWriter();
             CSVPrinter csvPrinter = new CSVPrinter(sw, CSVFormat.DEFAULT
                     .withHeader("ID", "Partner ID", "Phone Number", "Debit Account",
                             "Total Amount", "Installment Amount", "Start Date", "End Date",
                             "Frequency Type", "Frequency", "Status", "Status Code",
                             "Response Description", "Error Code", "Error Message",
                             "Submitted Date", "Created Date", "Failure Reason"))) {

            // Write each mandate record
            for (MandateReportDto dto : data) {
                csvPrinter.printRecord(
                        dto.getId(),
                        dto.getPartnerId() != null ? dto.getPartnerId() : "",
                        dto.getPhoneNumber() != null ? dto.getPhoneNumber() : "",
                        dto.getDebitAccount() != null ? dto.getDebitAccount() : "",
                        dto.getTotalAmount() != null ? dto.getTotalAmount().toString() : "",
                        dto.getInstallmentAmount() != null ? dto.getInstallmentAmount().toString() : "",
                        dto.getStartDate() != null ? dto.getStartDate().toString() : "",
                        dto.getEndDate() != null ? dto.getEndDate().toString() : "",
                        dto.getFrequencyType() != null ? dto.getFrequencyType() : "",
                        dto.getFrequency() != null ? dto.getFrequency().toString() : "",
                        dto.getStatus() != null ? dto.getStatus() : "",
                        dto.getStatusCode() != null ? dto.getStatusCode() : "",
                        dto.getResponseDescription() != null ? dto.getResponseDescription() : "",
                        dto.getErrorCode() != null ? dto.getErrorCode() : "",
                        dto.getErrorMessage() != null ? dto.getErrorMessage() : "",
                        dto.getSubmittedDate() != null ? dto.getSubmittedDate().toString() : "",
                        dto.getCreatedDate() != null ? dto.getCreatedDate().toString() : "",
                        dto.getFailureReason() != null ? dto.getFailureReason() : ""
                );
            }

            csvPrinter.flush();
            return sw.toString().getBytes();

        } catch (IOException e) {
            log.error("Error generating CSV report: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to generate CSV report", e);
        }
    }
}