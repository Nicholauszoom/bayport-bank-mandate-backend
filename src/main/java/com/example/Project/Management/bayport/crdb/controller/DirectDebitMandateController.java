package com.example.Project.Management.bayport.crdb.controller;

import com.example.Project.Management.bayport.crdb.dto.portal.DirectDebitMandateDto;
import com.example.Project.Management.bayport.crdb.entity.DirectDebitMandate;
import com.example.Project.Management.bayport.crdb.enumerated.MandateRequestType;
import com.example.Project.Management.bayport.crdb.service.CrdbDirectMandateService;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static com.example.Project.Management.bayport.util.DateFormatter.formatDateToYyMmDd;

@Controller
@Slf4j
@RequestMapping("/api/direct/debit/mandate")
public class DirectDebitMandateController {

    private final CrdbDirectMandateService crdbDirectMandateService;

    public DirectDebitMandateController(CrdbDirectMandateService crdbDirectMandateService) {
        this.crdbDirectMandateService = crdbDirectMandateService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> addProduct(@RequestBody DirectDebitMandateDto directDebitMandateDto) throws Exception {
        log.info("Direct Debit Mandate request: {}",directDebitMandateDto);
        return crdbDirectMandateService.registerDirectDebitMandateForm(directDebitMandateDto);
    }

    @PostMapping("/upload")
    public ResponseEntity<java.lang.String> uploadPriceScheduleExcel(@RequestParam("file") MultipartFile file) throws Exception {
        try {
            crdbDirectMandateService.saveDirectDebitMandateFromExcel(file);
            return ResponseEntity.status(HttpStatus.OK).body("Direct Debit Mandate uploaded successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/list")
//@CustomAnnotations.RequirePermission(Permission.MANAGE_USERS)
    public ResponseEntity<List<DirectDebitMandateDto>> getDebitMandateList(@RequestParam(required = false) MandateRequestType requestType) {
        try {
            List<DirectDebitMandate> mandates = crdbDirectMandateService.findDebitMandatesByRequestType(requestType);

            if (mandates == null || mandates.isEmpty()) {
                log.info("No mandates found");
                return ResponseEntity.ok(List.of());
            }

            List<DirectDebitMandateDto> directDebitMandateDtos = mandates.stream()
                    .map(mandate -> DirectDebitMandateDto.builder()
                            .id(mandate.getId())
                            .checksum(mandate.getChecksum())
                            .partnerId(mandate.getPartnerId())
                            .phoneNumber(mandate.getPhoneNumber())
                            .debitAccount(mandate.getDebitAccount())
                            .totalAmount(mandate.getTotalAmount())
                            .installmentAmount(mandate.getInstallmentAmount())
                            .startDate(String.valueOf(mandate.getStartDate()))
                            .endDate(String.valueOf(mandate.getEndDate()))
                            .frequencyType(mandate.getFrequencyType())
                            .frequency(Integer.valueOf(mandate.getFrequency()))
                            .billDetails(mandate.getBillDetails())
                            .createdDate(mandate.getCreatedDate())

                            .build()
                    )
                    .toList();

            log.info("Successfully fetched {} direct debit mandate", directDebitMandateDtos.size());
            return ResponseEntity.ok(directDebitMandateDtos);

        } catch (Exception e) {
            log.error("Error fetching direct debit mandate list: {}", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(List.of());
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<DirectDebitMandateDto>> searchDirectDebitMandate(
            @RequestParam(required = false) java.lang.String term, MandateRequestType requestType) {
        return ResponseEntity.ok(crdbDirectMandateService.searchDirectDebitMandate(term,requestType));
    }

    @GetMapping("/{id}")
//    @CustomAnnotations.RequirePermission(Permission.MANAGE_USERS)
    public ResponseEntity<?> getDirectDebitMandateById(@PathVariable long id) throws Exception {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            Optional<DirectDebitMandate> directDebitMandateOption = crdbDirectMandateService.findById(id);

            if (directDebitMandateOption == null || directDebitMandateOption.isEmpty()) {
                log.info("No direct debit mandate found");
                return ResponseEntity.status(HttpStatus.OK).body("No direct debit mandate found");
            }

            DirectDebitMandate directDebitMandate = directDebitMandateOption.get();

            DirectDebitMandateDto directDebitMandateDto = DirectDebitMandateDto.builder()
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
                    .mandateRequestType(directDebitMandate.getMandateRequestType())
                    .statusCode(directDebitMandate.getStatusCode())
                    .responseDescription(directDebitMandate.getResponseDescription())
                    .errorCode(directDebitMandate.getErrorCode())
                    .errorMessage(directDebitMandate.getErrorMessage())
                    .submittedDate(directDebitMandate.getSubmittedDate())
                    .createdDate(directDebitMandate.getCreatedDate())
                    .build();

            log.info("Successfully fetched direct debit mandate: {}", directDebitMandateDto);
            return ResponseEntity.ok(directDebitMandateDto);

        } catch (Exception e) {
            log.error("Error fetching direct debit mandate: {}", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching direct debit mandate :" + e.getMessage());
        }
    }

    @PostMapping("/submit/mandate")
    public ResponseEntity<?> subbmitMandate(@NonNull @RequestBody List<Long> ids) throws Exception {
        return crdbDirectMandateService.submitMandate(ids);
    }

    @PostMapping("/{id}/update")
    public ResponseEntity<?> updateMandate(@PathVariable("id") long id,
                                           @RequestBody DirectDebitMandateDto directDebitMandateDto) {
        try {
            log.info("details: {}", directDebitMandateDto);
            Optional<DirectDebitMandate> directDebitMandateOptional = crdbDirectMandateService.findById(id);
            if (directDebitMandateOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Mandate not found");
            }
            String requestType = directDebitMandateOptional.get().getMandateRequestType().name();

            if (requestType.equals(MandateRequestType.SUBMITTED)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Mandate NOT ALLOWED TO BE EDITED");
            }

            DirectDebitMandate directDebitMandate = directDebitMandateOptional.get();
            directDebitMandate.setPhoneNumber(directDebitMandateDto.getPhoneNumber());
            directDebitMandate.setDebitAccount(directDebitMandateDto.getDebitAccount());
            directDebitMandate.setTotalAmount(directDebitMandateDto.getTotalAmount());
            directDebitMandate.setInstallmentAmount(directDebitMandateDto.getInstallmentAmount());
//            directDebitMandate.setStartDate(ddMmYyStringToDate(directDebitMandateDto.getStartDate()));
//            directDebitMandate.setEndDate(ddMmYyStringToDate(directDebitMandateDto.getEndDate()));
            directDebitMandate.setFrequencyType(directDebitMandateDto.getFrequencyType());
            directDebitMandate.setFrequency(directDebitMandateDto.getFrequency());
            directDebitMandate.setBillDetails(directDebitMandateDto.getBillDetails());
            crdbDirectMandateService.save(directDebitMandate);
            log.info("updated direct mandate details: {}", directDebitMandate);

            return ResponseEntity.ok("DI9RECT MANDATE UPDATED SUCCESSFULLY");
        } catch (Exception e) {
            log.error("Update failed", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("DIRECT MANDATE FAILED TO UPDATE");
        }
    }

    @PostMapping("/{id}/delete")
    public ResponseEntity<?> deleteMandate(@PathVariable("id") long id) {
        try {
            Optional<DirectDebitMandate> mandateOpt = crdbDirectMandateService.findById(id);
            if (mandateOpt.isEmpty()) {
                log.info("Mandate not found with id {}", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("MANDATE NOT FOUND");
            }
            crdbDirectMandateService.deleteById(id);
            log.info("success delete mandate with id {}", id);
            return ResponseEntity.status(HttpStatus.OK).body("DELETE MANDATE SUCCESSFULLY");
        }
        catch (Exception e) {
        log.info("Delete failed: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.OK).body("FAIL TO DELETE MANDATE");
        }
    }

    @PostMapping("/delete/mandate")
    public ResponseEntity<?> deleteMandates(@NonNull @RequestBody List<Long> ids) throws Exception {
        return crdbDirectMandateService.deleteMandates(ids);
    }
}