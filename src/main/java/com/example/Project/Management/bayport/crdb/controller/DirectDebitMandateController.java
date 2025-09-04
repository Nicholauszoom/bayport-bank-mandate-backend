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

import java.util.List;
import java.util.Optional;

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
            List<DirectDebitMandate> mandates = crdbDirectMandateService.findDebitMandateByRequestType(requestType);

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
                            .startDate(mandate.getStartDate())
                            .endDate(mandate.getEndDate())
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
                    .startDate(directDebitMandate.getStartDate())
                    .endDate(directDebitMandate.getEndDate())
                    .frequencyType(directDebitMandate.getFrequencyType())
                    .frequency(Integer.valueOf(directDebitMandate.getFrequency()))
                    .billDetails(directDebitMandate.getBillDetails())
                    .mandateRequestType(directDebitMandate.getMandateRequestType())
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
}