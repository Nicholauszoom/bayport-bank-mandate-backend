package com.example.Project.Management.bayport.crdb.service;

import com.example.Project.Management.bayport.crdb.dto.DirectDebitMandateRequest;
import com.example.Project.Management.bayport.crdb.dto.DirectDebitMandateResponse;
import com.example.Project.Management.bayport.crdb.dto.portal.DirectDebitMandateDto;
import com.example.Project.Management.bayport.crdb.entity.DirectDebitMandate;
import com.example.Project.Management.bayport.crdb.enumerated.MandateRequestType;
import com.example.Project.Management.bayport.crdb.repository.DirectMandateRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.xml.bind.DatatypeConverter;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.*;

import static com.example.Project.Management.bayport.util.DateFormatter.*;

@Service
@Slf4j
public class CrdbDirectMandateService {
    @Value("${crdb.esb.api.url}") // e.g., https://<ESB API GW IP>/direct-debit/1.0.0/mandate/
    private String apiUrl;

    @Value("${crdb.partner.id}") //  PartnerId, e.g., "fcc19daa-68c9-482e-86cd-a5f1d64cb8d2" (replace with actual)
    private String partnerId;

    @Value("${crdb.hashing.salt}") // The long salt string, e.g., "kdaz9/m4nSLNUGoFLc5SPSKG/n63wMBTo9Ws3kvbXELm/D0gbAhJpeyQfvhp+Kv7i78TFg6BvOV+64LYax2JSCJcO5ySF1bLSraJUvYbgmUIMKIzP4TBIJQxzPULIHAylLbLMA=="
    private String hashingSalt;

    @Value("${crdb.channel}") // e.g., "SIMAPP" (adjust based on your channel)
    private String channel;

    @Value("${crdb.app.id}") // e.g., "SIMB_MOB"
    private String appId;

    @Value("${crdb.app.version}") // e.g., "1.0.0"
    private String appVersion;

    private final RestTemplate restTemplate;

    private final DirectMandateRepository  directMandateRepository;

    private final TokenManagementService tokenManagementService;

    public CrdbDirectMandateService(RestTemplate restTemplate, DirectMandateRepository directMandateRepository, TokenManagementService tokenManagementService) {
        this.restTemplate = restTemplate;
        this.directMandateRepository = directMandateRepository;
        this.tokenManagementService = tokenManagementService;
    }

    public DirectDebitMandateResponse initiateDirectDebit(DirectDebitMandateRequest request) throws NoSuchAlgorithmException {
        // Generate unique RequestId (UUID)
        String requestId = UUID.randomUUID().toString();

        // Set PartnerId from config if not provided
        if (request.getPartnerId() == null) {
            request.setPartnerId(partnerId);
        }

        // Generate Checksum
        String debitAccountLast4 = request.getDebitAccount().substring(request.getDebitAccount().length() - 4);
        String payloadToHash = requestId + debitAccountLast4 + request.getPartnerId() + hashingSalt;
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] digest = md.digest(payloadToHash.getBytes());
        String checksum = DatatypeConverter.printHexBinary(digest).toLowerCase();
        request.setChecksum(checksum);

        // Get valid token
        String accessToken = tokenManagementService.getValidToken();

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

        log.info("Prepared mandate request to be sent to CRDB: {}", entity);

        try {
            // Make the POST call
            ResponseEntity<DirectDebitMandateResponse> responseEntity = restTemplate.exchange(
                    apiUrl,
                    HttpMethod.POST,
                    entity,
                    DirectDebitMandateResponse.class
            );

            HttpStatus statusCode = (HttpStatus) responseEntity.getStatusCode();
            DirectDebitMandateResponse responseBody = responseEntity.getBody();

            log.info("Response from CRDB API - Status: {}, Body: {}", statusCode, responseBody);

            // Handle success responses (2xx)
            if (statusCode.is2xxSuccessful()) {
                log.info("✓ CRDB API call successful");
                return responseBody;
            }
            // Handle client errors (4xx)
            else if (statusCode.is4xxClientError()) {
                log.warn("✗ CRDB API returned client error: {}", statusCode);
                if (responseBody != null && responseBody.getResponseHeader() != null) {
                    log.warn("Error details - Code: {}, Message: {}",
                            responseBody.getResponseHeader().getErrorCode(),
                            responseBody.getResponseHeader().getErrorMessage());
                }
                // Return the error response so caller can handle it
                return responseBody;
            }
            // Handle server errors (5xx)
            else if (statusCode.is5xxServerError()) {
                log.error("✗ CRDB API returned server error: {}", statusCode);
                if (responseBody != null && responseBody.getResponseHeader() != null) {
                    log.error("Error details - Code: {}, Message: {}",
                            responseBody.getResponseHeader().getErrorCode(),
                            responseBody.getResponseHeader().getErrorMessage());
                }
                // Return the error response so caller can handle it
                return responseBody;
            }
            // Handle unexpected status codes
            else {
                log.error("✗ CRDB API returned unexpected status: {}", statusCode);
                throw new RuntimeException("Unexpected API response status: " + statusCode);
            }

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            // This should not happen with our custom error handler, but just in case
            log.error("HTTP Error calling CRDB API: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());

            // Try to parse error response
            try {
                ObjectMapper mapper = new ObjectMapper();
                DirectDebitMandateResponse errorResponse = mapper.readValue(
                        e.getResponseBodyAsString(),
                        DirectDebitMandateResponse.class
                );
                return errorResponse;
            } catch (Exception parseException) {
                log.error("Failed to parse error response", parseException);
                throw new RuntimeException("API call failed with status: " + e.getStatusCode(), e);
            }
        } catch (Exception e) {
            log.error("Unexpected error calling CRDB API: {}", e.getMessage(), e);
            throw new RuntimeException("API call failed: " + e.getMessage(), e);
        }
    }
    public void saveDirectDebitMandateFromExcel(MultipartFile file) throws Exception {
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

    private String getStringValue(Cell cell, FormulaEvaluator evaluator) {
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
                            return String.valueOf((long) numericValue);
                        } else {
                            return String.valueOf(numericValue);
                        }
                    }
                case BOOLEAN:
                    return String.valueOf(cell.getBooleanCellValue());
                case FORMULA:
                    CellValue evaluatedValue = evaluator.evaluate(cell);
                    switch (evaluatedValue.getCellType()) {
                        case STRING:
                            return evaluatedValue.getStringValue().trim();
                        case NUMERIC:
                            double value = evaluatedValue.getNumberValue();
                            if (value == Math.floor(value)) {
                                return String.valueOf((long) value);
                            } else {
                                return String.valueOf(value);
                            }
                        case BOOLEAN:
                            return String.valueOf(evaluatedValue.getBooleanValue());
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
                    String strValue = cell.getStringCellValue().trim();
                    if (strValue.isEmpty()) return null;
                    return new BigDecimal(strValue).setScale(4, RoundingMode.HALF_UP);
                case FORMULA:
                    CellValue evaluatedValue = evaluator.evaluate(cell);
                    if (evaluatedValue.getCellType() == CellType.NUMERIC) {
                        return BigDecimal.valueOf(evaluatedValue.getNumberValue()).setScale(4, RoundingMode.HALF_UP);
                    } else if (evaluatedValue.getCellType() == CellType.STRING) {
                        String strVal = evaluatedValue.getStringValue().trim();
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
                    String strValue = cell.getStringCellValue().trim();
                    if (strValue.isEmpty()) return 0.0;
                    return Double.parseDouble(strValue);
                case FORMULA:
                    CellValue evaluatedValue = evaluator.evaluate(cell);
                    if (evaluatedValue.getCellType() == CellType.NUMERIC) {
                        return evaluatedValue.getNumberValue();
                    } else if (evaluatedValue.getCellType() == CellType.STRING) {
                        String strVal = evaluatedValue.getStringValue().trim();
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
                    String strValue = cell.getStringCellValue().trim();
                    if (strValue.isEmpty()) return 0;
                    return (int) Math.round(Double.parseDouble(strValue));
                case FORMULA:
                    CellValue evaluatedValue = evaluator.evaluate(cell);
                    if (evaluatedValue.getCellType() == CellType.NUMERIC) {
                        return (int) Math.round(evaluatedValue.getNumberValue());
                    } else if (evaluatedValue.getCellType() == CellType.STRING) {
                        String strVal = evaluatedValue.getStringValue().trim();
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
                .startDate(formatDateToYyMmDd(directDebitMandate.getStartDate()))
                .endDate(formatDateToYyMmDd(directDebitMandate.getEndDate()))
                .frequencyType(directDebitMandate.getFrequencyType())
                .frequency(Integer.valueOf(directDebitMandate.getFrequency()))
                .billDetails(directDebitMandate.getBillDetails())
                .createdDate(directDebitMandate.getCreatedDate())
                .build();
    }

    public Page<DirectDebitMandate> findDebitMandateByRequestType(MandateRequestType requestType, Pageable pageable) {
        if (requestType == null) {
            return directMandateRepository.findAll(pageable);
        } else {
            return directMandateRepository.findByRequestTypeWithPageableSort(requestType, pageable);
        }
    }

    public Optional<DirectDebitMandate> findById(long id) {
        return directMandateRepository.findById(id);
    }

    public ResponseEntity<?> registerDirectDebitMandateForm(DirectDebitMandateDto directDebitMandateDto) {
       try{
           String checksum = generateChecksumByMandateDto(directDebitMandateDto);

           DirectDebitMandate directDebitMandate = DirectDebitMandate.builder()
                   .checksum(checksum)
                   .partnerId(partnerId)
                   .phoneNumber(directDebitMandateDto.getPhoneNumber())
                   .debitAccount(directDebitMandateDto.getDebitAccount())
                   .totalAmount(directDebitMandateDto.getTotalAmount())
                   .installmentAmount(directDebitMandateDto.getInstallmentAmount())
                   .startDate(ddMmYyStringToDate(directDebitMandateDto.getStartDate()))
                   .endDate(ddMmYyStringToDate(directDebitMandateDto.getEndDate()))
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

    public String generateChecksumByMandateDto(DirectDebitMandateDto request) throws Exception {
        // Generate unique RequestId (UUID)
        String requestId = UUID.randomUUID().toString();
        String debitAccountLast4 = request.getDebitAccount().substring(request.getDebitAccount().length() - 4);
        String payloadToHash = requestId + debitAccountLast4 + request.getPartnerId() + hashingSalt;
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] digest = md.digest(payloadToHash.getBytes());
        String checksum = DatatypeConverter.printHexBinary(digest).toLowerCase();
        return checksum;
    }

    public String generateChecksumByMandate(DirectDebitMandate request) throws Exception {
        // Generate unique RequestId (UUID)
        String requestId = UUID.randomUUID().toString();
        String debitAccountLast4 = request.getDebitAccount().substring(request.getDebitAccount().length() - 4);
        String payloadToHash = requestId + debitAccountLast4 + request.getPartnerId() + hashingSalt;
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] digest = md.digest(payloadToHash.getBytes());
        String checksum = DatatypeConverter.printHexBinary(digest).toLowerCase();
        return checksum;
    }

    public ResponseEntity<?> submitMandate(List<Long> ids) {
        try {
            log.info("submitMandate request: {}", ids);
            LocalDateTime now = LocalDateTime.now();
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
                        .startDate(formatDateToYyMmDd(directDebitMandate.getStartDate()))
                        .endDate(formatDateToYyMmDd(directDebitMandate.getEndDate()))
                        .frequencyType(directDebitMandate.getFrequencyType())
                        .frequency(directDebitMandate.getFrequency())
                        .billDetails(directDebitMandate.getBillDetails())
                        .build();

                // USED EVEN TO PRODUCTION AND TEST
                directDebitMandate.setMandateRequestType(MandateRequestType.PRESUBMITTED);
                directMandateRepository.save(directDebitMandate);

                // FOR TEST PURPOSES
//                log.info("submitMandate request to CRDB: {}", directDebitMandateRequest);
//                directDebitMandate.setStatusCode("1000");
//                directDebitMandate.setResponseDescription("OK");
//                directDebitMandate.setMandateRequestType(MandateRequestType.SUBMITTED);
//                directDebitMandate.setSubmittedDate(String.valueOf(now));
//                directMandateRepository.save(directDebitMandate);

                // FOR PRODUCTION PURPOSES
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
                        log.info("Mandate General success request-response: {}", directDebitMandateResponse);

                        // update mandate
                        directDebitMandate.setStatusCode(String.valueOf(header.getStatusCode()));
                        directDebitMandate.setResponseDescription(header.getStatusDesc());
                        directDebitMandate.setMandateRequestType(MandateRequestType.SUBMITTED);
                        directDebitMandate.setSubmittedDate(String.valueOf(now));
                        directMandateRepository.save(directDebitMandate);
                    } else {
                        log.info("Mandate General fail request-response: {}", directDebitMandateResponse);
                        log.error("Mandate request failed: {} - {}",
                                header.getStatusCode(),
                                header.getStatusMessage());

                        // update mandate
                        directDebitMandate.setMandateRequestType(MandateRequestType.FAILED);
                        directDebitMandate.setStatusCode(String.valueOf(header.getStatusCode()));
                        directDebitMandate.setErrorCode(String.valueOf(header.getErrorCode()));
                        directDebitMandate.setErrorMessage(header.getErrorMessage());
                        directDebitMandate.setResponseDescription(header.getStatusDesc());
                        directDebitMandate.setSubmittedDate(String.valueOf(now));
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

    public void save(DirectDebitMandate directDebitMandate) {
        directMandateRepository.save(directDebitMandate);
    }

    public List<DirectDebitMandate> findAll() {
        List<DirectDebitMandate> mandates = directMandateRepository.findAll();
        return mandates;
    }

    public void deleteById(long id) {
        Optional<DirectDebitMandate> directDebitMandateOptional = directMandateRepository.findById(id);
        if (directDebitMandateOptional.isEmpty()) {
            log.info("Mandate not found with id: {}", id);
        }
        String requestType = directDebitMandateOptional.get().getMandateRequestType().name();
        if (requestType.equals(MandateRequestType.SUBMITTED)) {
            log.info("Mandate not allowed to be deleted coz Request Type is SUBMITTED: {}", id);
        }
        directMandateRepository.deleteById(id);
    }

    public ResponseEntity<?> deleteMandates(@NonNull List<Long> ids) {
        try {
            log.info("delete Mandate request: {}", ids);
            for (Long id : ids)  {
                Optional<DirectDebitMandate> directDebitMandateOptional = directMandateRepository.findById(id);
                if (directDebitMandateOptional.isEmpty()) {
                    log.info("Mandate not found with id: {}", id);
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("MANDATE NOT FOUIND");
                }
                directMandateRepository.deleteById(id);
            }

            log.info("ALL ITEMS DELETED SUCCESSFULLY: {}");
            return ResponseEntity.status(HttpStatus.OK).body("ALL ITEMS DELETED SUCCESSFULLY");
        } catch (Exception e) {
            log.info("Delete Mandate request failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("FAIL TO DELETE");
        }
    }

    public Page<DirectDebitMandate> findDebitMandatesByRequestType(MandateRequestType mandateRequestType, Pageable pageable) {
        return directMandateRepository.findDebitMandateByRequestType(mandateRequestType, pageable);
    }

    public List<DirectDebitMandate> findDebitMandatesByRequestTypeNonPageable(MandateRequestType mandateRequestType) {
        return directMandateRepository.findDebitMandatesByRequestTypeNonPageable(mandateRequestType);
    }
}