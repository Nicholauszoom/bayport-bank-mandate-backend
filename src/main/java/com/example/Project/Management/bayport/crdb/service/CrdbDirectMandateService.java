package com.example.Project.Management.bayport.crdb.service;

import com.example.Project.Management.bayport.crdb.dto.DirectDebitMandateRequest;
import com.example.Project.Management.bayport.crdb.dto.DirectDebitMandateResponse;
import com.example.Project.Management.bayport.crdb.dto.portal.DirectDebitMandateDto;
import com.example.Project.Management.bayport.crdb.entity.DirectDebitMandate;
import com.example.Project.Management.bayport.crdb.enumerated.MandateRequestType;
import com.example.Project.Management.bayport.crdb.repository.DirectMandateRepository;
import jakarta.xml.bind.DatatypeConverter;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@Service
@Slf4j
public class CrdbDirectMandateService {
    @Value("${crdb.esb.api.url}") // e.g., https://<ESB API GW IP>/direct-debit/1.0.0/mandate/
    private java.lang.String apiUrl;

    @Value("${crdb.partner.id}") // Your PartnerId, e.g., "fcc19daa-68c9-482e-86cd-a5f1d64cb8d2" (replace with actual)
    private java.lang.String partnerId;

    @Value("${crdb.hashing.salt}") // The long salt string, e.g., "kdaz9/m4nSLNUGoFLc5SPSKG/n63wMBTo9Ws3kvbXELm/D0gbAhJpeyQfvhp+Kv7i78TFg6BvOV+64LYax2JSCJcO5ySF1bLSraJUvYbgmUIMKIzP4TBIJQxzPULIHAylLbLMA=="
    private java.lang.String hashingSalt;

    @Value("${crdb.access.token}") // Your Bearer token (manage securely, e.g., via OAuth or env var)
    private java.lang.String accessToken;

    @Value("${crdb.channel}") // e.g., "SIMAPP" (adjust based on your channel)
    private java.lang.String channel;

    @Value("${crdb.app.id}") // e.g., "SIMB_MOB"
    private java.lang.String appId;

    @Value("${crdb.app.version}") // e.g., "1.0.0"
    private java.lang.String appVersion;

    private final RestTemplate restTemplate;

    private final DirectMandateRepository  directMandateRepository;

    public CrdbDirectMandateService(RestTemplate restTemplate, DirectMandateRepository directMandateRepository) {
        this.restTemplate = restTemplate;
        this.directMandateRepository = directMandateRepository;
    }

    public DirectDebitMandateResponse initiateDirectDebit(DirectDebitMandateRequest request) throws NoSuchAlgorithmException {
        // Generate unique RequestId (UUID)
        java.lang.String requestId = UUID.randomUUID().toString();

        // Set PartnerId from config if not provided
        if (request.getPartnerId() == null) {
            request.setPartnerId(partnerId);
        }

        // Generate Checksum
        java.lang.String debitAccountLast4 = request.getDebitAccount().substring(request.getDebitAccount().length() - 4);
        java.lang.String payloadToHash = requestId + debitAccountLast4 + request.getPartnerId() + hashingSalt;
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] digest = md.digest(payloadToHash.getBytes());
        java.lang.String checksum = DatatypeConverter.printHexBinary(digest).toLowerCase();
        request.setChecksum(checksum);

        // Prepare headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set("Channel", channel);
        headers.set("AppId", appId);
        headers.set("AppVersion", appVersion);
        headers.set("RequestId", requestId);
        headers.set("Authorization", "Bearer " + accessToken);

        // Prepare entity
        HttpEntity<DirectDebitMandateRequest> entity = new HttpEntity<>(request, headers);

        log.info("Prepared mandate request to nbe sent to CRDB {}: ", entity.toString());
        // Make the POST call
        ResponseEntity<DirectDebitMandateResponse> responseEntity = restTemplate.exchange(
                apiUrl,
                HttpMethod.POST,
                entity,
                DirectDebitMandateResponse.class
        );

        log.info("Response from CRDB API is {}", responseEntity.getBody());

        // Handle response (you can add error handling based on status code)
        if (!responseEntity.getStatusCode().is2xxSuccessful()) {
            log.info("HTTP Status Code: " + responseEntity.getStatusCode());
            // Throw custom exception or handle errors
            throw new RuntimeException("API call failed with status: " + responseEntity.getStatusCode());
        }

        return responseEntity.getBody();
    }

    public void saveDirectDebitMandateFromExcel(MultipartFile file) throws Exception {

        List<DirectDebitMandate> directDebitMandates = new ArrayList<>();

        try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);

            FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null || isRowEmpty(row)) {
                    continue;
                }
                try {
                    DirectDebitMandate directDebitMandate = new DirectDebitMandate();
                    directDebitMandate.setPartnerId(partnerId);
                    directDebitMandate.setPhoneNumber(getStringValue(row.getCell(0), evaluator));
                    directDebitMandate.setDebitAccount(getStringValue(row.getCell(1), evaluator));
                    directDebitMandate.setTotalAmount(getBigDecimalValue(row.getCell(2), evaluator));
                    directDebitMandate.setInstallmentAmount(getBigDecimalValue(row.getCell(3), evaluator));
                    directDebitMandate.setStartDate(getStringValue(row.getCell(4), evaluator));
                    directDebitMandate.setEndDate(getStringValue(row.getCell(5), evaluator));
                    directDebitMandate.setBillDetails(getStringValue(row.getCell(6), evaluator));
                    directDebitMandate.setFrequencyType(getStringValue(row.getCell(7), evaluator));
                    directDebitMandate.setFrequency(getIntValue(row.getCell(8), evaluator));
                    directDebitMandate.setMandateRequestType(MandateRequestType.UPLOADED);
                    directDebitMandate.setChecksum(generateChecksumByMandate(directDebitMandate));
                    directDebitMandates.add(directDebitMandate);
                } catch (Exception e) {
                    log.warn("Error processing row {}: {}", i, e.getMessage());
                    // Continue processing other rows
                }
            }
        }

        if (!directDebitMandates.isEmpty()) {
            directMandateRepository.saveAll(directDebitMandates);
            log.info("Successfully saved {} mandate records", directDebitMandates.size());
        } else {
            log.warn("No valid mandate records found in the Excel file");
        }
    }

    private boolean isRowEmpty(Row row) {
        for (int cellNum = row.getFirstCellNum(); cellNum < row.getLastCellNum(); cellNum++) {
            Cell cell = row.getCell(cellNum);
            if (cell != null && cell.getCellType() != CellType.BLANK) {
                return false;
            }
        }
        return true;
    }

    private java.lang.String getStringValue(Cell cell, FormulaEvaluator evaluator) {
        if (cell == null) return null;

        try {
            switch (cell.getCellType()) {
                case STRING:
                    return cell.getStringCellValue().trim();
                case NUMERIC:
                    if (DateUtil.isCellDateFormatted(cell)) {
                        return cell.getDateCellValue().toString();
                    } else {
                        double numericValue = cell.getNumericCellValue();
                        if (numericValue == Math.floor(numericValue)) {
                            return java.lang.String.valueOf((long) numericValue);
                        } else {
                            return java.lang.String.valueOf(numericValue);
                        }
                    }
                case BOOLEAN:
                    return java.lang.String.valueOf(cell.getBooleanCellValue());
                case FORMULA:
                    CellValue evaluatedValue = evaluator.evaluate(cell);
                    switch (evaluatedValue.getCellType()) {
                        case STRING:
                            return evaluatedValue.getStringValue().trim();
                        case NUMERIC:
                            double value = evaluatedValue.getNumberValue();
                            if (value == Math.floor(value)) {
                                return java.lang.String.valueOf((long) value);
                            } else {
                                return java.lang.String.valueOf(value);
                            }
                        case BOOLEAN:
                            return java.lang.String.valueOf(evaluatedValue.getBooleanValue());
                        default:
                            return null;
                    }
                default:
                    return null;
            }
        } catch (Exception e) {
            log.warn("Error reading string value from cell: {}", e.getMessage());
            return null;
        }
    }

    private BigDecimal getBigDecimalValue(Cell cell, FormulaEvaluator evaluator) {
        if (cell == null) return null;

        try {
            switch (cell.getCellType()) {
                case NUMERIC:
                    return BigDecimal.valueOf(cell.getNumericCellValue()).setScale(4, RoundingMode.HALF_UP);
                case STRING:
                    java.lang.String strValue = cell.getStringCellValue().trim();
                    if (strValue.isEmpty()) return null;
                    return new BigDecimal(strValue).setScale(4, RoundingMode.HALF_UP);
                case FORMULA:
                    CellValue evaluatedValue = evaluator.evaluate(cell);
                    if (evaluatedValue.getCellType() == CellType.NUMERIC) {
                        return BigDecimal.valueOf(evaluatedValue.getNumberValue()).setScale(4, RoundingMode.HALF_UP);
                    } else if (evaluatedValue.getCellType() == CellType.STRING) {
                        java.lang.String strVal = evaluatedValue.getStringValue().trim();
                        if (strVal.isEmpty()) return null;
                        return new BigDecimal(strVal).setScale(4, RoundingMode.HALF_UP);
                    }
                    return null;
                default:
                    return null;
            }
        } catch (NumberFormatException e) {
            log.warn("Error parsing BigDecimal from cell: {}", e.getMessage());
            return null;
        }
    }

    private Double getDoubleValue(Cell cell, FormulaEvaluator evaluator) {
        if (cell == null) return 0.0;

        try {
            switch (cell.getCellType()) {
                case NUMERIC:
                    return cell.getNumericCellValue();
                case STRING:
                    java.lang.String strValue = cell.getStringCellValue().trim();
                    if (strValue.isEmpty()) return 0.0;
                    return Double.parseDouble(strValue);
                case FORMULA:
                    CellValue evaluatedValue = evaluator.evaluate(cell);
                    if (evaluatedValue.getCellType() == CellType.NUMERIC) {
                        return evaluatedValue.getNumberValue();
                    } else if (evaluatedValue.getCellType() == CellType.STRING) {
                        java.lang.String strVal = evaluatedValue.getStringValue().trim();
                        if (strVal.isEmpty()) return 0.0;
                        return Double.parseDouble(strVal);
                    }
                    return 0.0;
                default:
                    return 0.0;
            }
        } catch (NumberFormatException e) {
            log.warn("Error parsing Double from cell: {}", e.getMessage());
            return 0.0;
        }
    }

    private Integer getIntValue(Cell cell, FormulaEvaluator evaluator) {
        if (cell == null) return 0;

        try {
            switch (cell.getCellType()) {
                case NUMERIC:
                    return (int) Math.round(cell.getNumericCellValue());
                case STRING:
                    java.lang.String strValue = cell.getStringCellValue().trim();
                    if (strValue.isEmpty()) return 0;
                    return (int) Math.round(Double.parseDouble(strValue));
                case FORMULA:
                    CellValue evaluatedValue = evaluator.evaluate(cell);
                    if (evaluatedValue.getCellType() == CellType.NUMERIC) {
                        return (int) Math.round(evaluatedValue.getNumberValue());
                    } else if (evaluatedValue.getCellType() == CellType.STRING) {
                        java.lang.String strVal = evaluatedValue.getStringValue().trim();
                        if (strVal.isEmpty()) return 0;
                        return (int) Math.round(Double.parseDouble(strVal));
                    }
                    return 0;
                default:
                    return 0;
            }
        } catch (NumberFormatException e) {
            log.warn("Error parsing Integer from cell: {}", e.getMessage());
            return 0;
        }
    }

    public List<DirectDebitMandateDto> searchDirectDebitMandate(java.lang.String term, MandateRequestType requestType) {
        List<DirectDebitMandate> mandates = directMandateRepository.searchDirectDebitMandate(term, requestType);
        return mandates.stream()
                .map(this::mapToDto)
                .toList();
    }

    private DirectDebitMandateDto mapToDto(DirectDebitMandate directDebitMandate) {
        return DirectDebitMandateDto.builder()
                .id(directDebitMandate.getId())
                .checksum(directDebitMandate.getChecksum())
                .partnerId(directDebitMandate.getPartnerId())
                .phoneNumber(directDebitMandate.getPhoneNumber())
                .debitAccount(directDebitMandate.getDebitAccount())
                .totalAmount(directDebitMandate.getTotalAmount())
                .installmentAmount(directDebitMandate.getInstallmentAmount())
                .startDate(directDebitMandate.getStartDate())
                .endDate(directDebitMandate.getEndDate())
                .frequencyType(directDebitMandate.getFrequencyType())
                .frequency(Integer.valueOf(directDebitMandate.getFrequency()))
                .billDetails(directDebitMandate.getBillDetails())
                .build();
    }


    public List<DirectDebitMandate> findDebitMandateByRequestType(MandateRequestType requestType) {
    return directMandateRepository.findDebitMandateByRequestType(requestType);
    }

    public Optional<DirectDebitMandate> findById(long id) {
        return directMandateRepository.findById(id);
    }

    public ResponseEntity<?> registerDirectDebitMandateForm(DirectDebitMandateDto directDebitMandateDto) {
       try{
           java.lang.String checksum = generateChecksumByMandateDto(directDebitMandateDto);

           DirectDebitMandate directDebitMandate = DirectDebitMandate.builder()
                   .checksum(checksum)
                   .partnerId(partnerId)
                   .phoneNumber(directDebitMandateDto.getPhoneNumber())
                   .debitAccount(directDebitMandateDto.getDebitAccount())
                   .totalAmount(directDebitMandateDto.getTotalAmount())
                   .installmentAmount(directDebitMandateDto.getInstallmentAmount())
                   .startDate(directDebitMandateDto.getStartDate())
                   .endDate(directDebitMandateDto.getEndDate())
                   .frequencyType(directDebitMandateDto.getFrequencyType())
                   .frequency(directDebitMandateDto.getFrequency())
                   .billDetails(directDebitMandateDto.getBillDetails())
                   .build();
           log.info("mandate registration success: {}", directDebitMandateDto);
           return ResponseEntity.status(HttpStatus.OK).body("MANDATE REGISTRATION SUCCESSFUL");
       } catch (Exception e){
           log.info("Error registering direct debit mandate: {}", e.getMessage());
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("MANDATE REGISTRATION FAIL");
       }
    }

    public java.lang.String generateChecksumByMandateDto(DirectDebitMandateDto request) throws Exception {
        // Generate unique RequestId (UUID)
        java.lang.String requestId = UUID.randomUUID().toString();
        java.lang.String debitAccountLast4 = request.getDebitAccount().substring(request.getDebitAccount().length() - 4);
        java.lang.String payloadToHash = requestId + debitAccountLast4 + request.getPartnerId() + hashingSalt;
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] digest = md.digest(payloadToHash.getBytes());
        java.lang.String checksum = DatatypeConverter.printHexBinary(digest).toLowerCase();
        return checksum;
    }

    public java.lang.String generateChecksumByMandate(DirectDebitMandate request) throws Exception {
        // Generate unique RequestId (UUID)
        java.lang.String requestId = UUID.randomUUID().toString();
        java.lang.String debitAccountLast4 = request.getDebitAccount().substring(request.getDebitAccount().length() - 4);
        java.lang.String payloadToHash = requestId + debitAccountLast4 + request.getPartnerId() + hashingSalt;
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] digest = md.digest(payloadToHash.getBytes());
        java.lang.String checksum = DatatypeConverter.printHexBinary(digest).toLowerCase();
        return checksum;
    }

    public ResponseEntity<?> submitMandate(List<Long> ids) {
        try {
            log.info("submitMandate request: {}", ids);
            for (Long id : ids) {
                Optional<DirectDebitMandate> directDebitMandateOptional = directMandateRepository.findById(id);
                if (directDebitMandateOptional.isEmpty()) {
                    log.info("Mandate not found with id: {}", id);
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("MANDATE NOT FOUIND");
                }
                DirectDebitMandate directDebitMandate = directDebitMandateOptional.get();
                DirectDebitMandateRequest directDebitMandateRequest = DirectDebitMandateRequest.builder()
                        .checksum(directDebitMandate.getChecksum())
                        .partnerId(directDebitMandate.getPartnerId())
                        .phoneNumber(directDebitMandate.getPhoneNumber())
                        .debitAccount(directDebitMandate.getDebitAccount())
                        .totalAmount(directDebitMandate.getTotalAmount())
                        .installmentAmount(directDebitMandate.getInstallmentAmount())
                        .startDate(directDebitMandate.getStartDate())
                        .endDate(directDebitMandate.getEndDate())
                        .frequencyType(directDebitMandate.getFrequencyType())
                        .frequency(directDebitMandate.getFrequency())
                        .billDetails(directDebitMandate.getBillDetails())
                        .build();

                DirectDebitMandateResponse directDebitMandateResponse = initiateDirectDebit(directDebitMandateRequest);
                log.info("direct debit mandate response: {}", directDebitMandateResponse);

                if (directDebitMandateResponse != null
                        && directDebitMandateResponse.getResponseHeader()!= null) {

                    DirectDebitMandateResponse.ResponseHeader header = directDebitMandateResponse.getResponseHeader();

                    if (header.getStatusCode() != null && header.getStatusCode() == 1000) {
                        log.info("Mandate request successful: {}", directDebitMandateResponse);

                        DirectDebitMandateResponse.ResponseBody body = directDebitMandateResponse.getResponseBody();
                        if (body != null) {
                            log.info("Reference ID: {}", body.getReference());
                            log.info("Body RequestId: {}", body.getRequestId());
                        }
                        // update mandate
                        directDebitMandate.setMandateRequestType(MandateRequestType.SUBMITTED);
                        directMandateRepository.save(directDebitMandate);
                    } else {
                        log.error("Mandate request failed: {} - {}",
                                header.getStatusCode(),
                                header.getStatusMessage());

                        // update mandate
                        directDebitMandate.setMandateRequestType(MandateRequestType.FAILED);
                        directMandateRepository.save(directDebitMandate);
                    }

                } else {
                    log.error("Empty or invalid response from direct debit API");
                    // update mandate
                    directDebitMandate.setMandateRequestType(MandateRequestType.FAILED);
                    directMandateRepository.save(directDebitMandate);
                }
            }

            log.info("Successfull send mandate request to CRDB : {}");
            return ResponseEntity.status(HttpStatus.OK).body("SUCCESS SEND MANDATE TO CRDB");
        } catch (Exception e) {
            log.info("Fail to send mandate to CRDB due to: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("FAIL TO SEND MANDATE TO CRDB");
        }
    }
}
