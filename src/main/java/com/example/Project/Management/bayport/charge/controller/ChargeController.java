package com.example.Project.Management.bayport.charge.controller;

import com.example.Project.Management.bayport.charge.dto.portal.ChargeDto;
import com.example.Project.Management.bayport.charge.entity.Charge;
import com.example.Project.Management.bayport.charge.service.ChargeService;
import com.example.Project.Management.bayport.charge.service.PriceClientRvService;
import com.example.Project.Management.bayport.product.dto.portal.ProductDto;
import com.example.Project.Management.bayport.product.entity.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/charge")
@Slf4j
public class ChargeController {

    private final PriceClientRvService priceClientRvService;

    private final ChargeService chargeService;

    public ChargeController(PriceClientRvService priceClientRvService, ChargeService chargeService) {
        this.priceClientRvService = priceClientRvService;
        this.chargeService = chargeService;
    }

    @PostMapping("/price-schedule/upload")
    public ResponseEntity<String> uploadPriceScheduleExcel(@RequestParam("file") MultipartFile file) throws Exception {
        try {
            priceClientRvService.savePriceScheduleFromExcel(file);
            return ResponseEntity.ok("Price schedule uploaded successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/client-rv/upload")
    public ResponseEntity<String> uploadClientRvExcel(@RequestParam("file") MultipartFile file) throws Exception {
        try {
            priceClientRvService.saveClientRvFromExcel(file);
            return ResponseEntity.ok("Client Rv uploaded successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }


    @GetMapping("/list")
//@CustomAnnotations.RequirePermission(Permission.MANAGE_USERS)
    public ResponseEntity<List<ChargeDto>> getChargeList() {
        try {
            List<Charge> charges = chargeService.findAll();

            if (charges == null || charges.isEmpty()) {
                log.info("No charges found");
                return ResponseEntity.ok(List.of());
            }

            List<ChargeDto> chargeDtos = charges.stream()
                    .map(charge -> ChargeDto.builder()
                            .id(charge.getId())
                            .checkNumber(charge.getCheckNumber())
                            .designationCode(charge.getDesignationCode())
                            .designationName(charge.getDesignationName())
                            .basicSalary(charge.getBasicSalary())
                            .netSalary(charge.getNetSalary())
                            .oneThirdAmount(charge.getOneThirdAmount())
                            .requestedAmount(charge.getRequestedAmount())
                            .deductibleAmount(charge.getDeductibleAmount())
                            .desiredDeductibleAmount(charge.getDesiredDeductibleAmount())
                            .retirementDate(charge.getRetirementDate())
                            .termsOfEmployment(charge.getTermsOfEmployment())
                            .tenure(charge.getTenure())
                            .productCode(charge.getProductCode())
                            .voteCode(charge.getVoteCode())
                            .totalEmployeeDeduction(charge.getTotalEmployeeDeduction())
                            .jobClassCode(charge.getJobClassCode())
                            .createdDate(charge.getCreatedDate())
                            .build()
                    )
                    .toList();

            log.info("Successfully fetched {} charges", chargeDtos.size());
            return ResponseEntity.ok(chargeDtos);

        } catch (Exception e) {
            log.error("Error fetching charge list", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(List.of());
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<ChargeDto>> searchCharges(
            @RequestParam(required = false) String term) {
        return ResponseEntity.ok(chargeService.searchCharges(term));
    }

    @GetMapping("/{id}")
//    @CustomAnnotations.RequirePermission(Permission.MANAGE_USERS)
    public ResponseEntity<?> getChargeById(@PathVariable long id) throws Exception {
        try {
            Optional<Charge> chargeOption = chargeService.findById(id);

            if (chargeOption == null || chargeOption.isEmpty()) {
                log.info("No charge found");
                return ResponseEntity.status(HttpStatus.OK).body("No charge found");
            }
            Charge charge = chargeOption.get();

            ChargeDto chargeDto = ChargeDto.builder()
                    .id(charge.getId())
                    .checkNumber(charge.getCheckNumber())
                    .designationCode(charge.getDesignationCode())
                    .designationName(charge.getDesignationName())
                    .basicSalary(charge.getBasicSalary())
                    .netSalary(charge.getNetSalary())
                    .oneThirdAmount(charge.getOneThirdAmount())
                    .requestedAmount(charge.getRequestedAmount())
                    .deductibleAmount(charge.getDeductibleAmount())
                    .desiredDeductibleAmount(charge.getDesiredDeductibleAmount())
                    .retirementDate(charge.getRetirementDate())
                    .termsOfEmployment(charge.getTermsOfEmployment())
                    .tenure(charge.getTenure())
                    .productCode(charge.getProductCode())
                    .voteCode(charge.getVoteCode())
                    .totalEmployeeDeduction(charge.getTotalEmployeeDeduction())
                    .jobClassCode(charge.getJobClassCode())
                    .createdDate(charge.getCreatedDate())
                    // Response fields
                    .totalInsurance(charge.getTotalInsurance())
                    .totalProcessingFee(charge.getTotalProcessingFee())
                    .totalInterestRateAmount(charge.getTotalInterestRateAmount())
                    .otherCharges(charge.getOtherCharges())
                    .netLoanAmount(charge.getNetLoanAmount())
                    .totalAmountToPay(charge.getTotalAmountToPay())
                    .eligibleAmount(charge.getEligibleAmount())
                    .monthlyReturnAmount(charge.getMonthlyReturnAmount())
                    .responseTenure(charge.getResponseTenure())
                    .takeHomeAmount(charge.getTakeHomeAmount())
                    .insuranceProcessFee(charge.getInsuranceProcessFee())
                    .build();

            log.info("Successfully fetched charge: {}", chargeDto);
            return ResponseEntity.ok(chargeDto);

        } catch (Exception e) {
            log.error("Error fetching charge: {}", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching charge :" + e.getMessage());
        }
    }

    @PostMapping("/{id}/delete")
    public void deleteCharge(
            @PathVariable("id") long id) {
         chargeService.deleteCharge(id);
    }
}