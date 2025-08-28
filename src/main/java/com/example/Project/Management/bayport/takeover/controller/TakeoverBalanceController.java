package com.example.Project.Management.bayport.takeover.controller;

import com.example.Project.Management.bayport.takeover.dto.TakeoverBalanceDto;
import com.example.Project.Management.bayport.takeover.service.TakeoverBalanceService;
import com.example.Project.Management.bayport.takeover.entity.Takeoverbalance;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/takeover/balance")
@Slf4j
public class TakeoverBalanceController {
    private final TakeoverBalanceService takeoverBalanceService;

    public TakeoverBalanceController(TakeoverBalanceService takeoverBalanceService) {
        this.takeoverBalanceService = takeoverBalanceService;
    }


    @GetMapping("/list")
//@CustomAnnotations.RequirePermission(Permission.MANAGE_USERS)
    public ResponseEntity<List<TakeoverBalanceDto>> getTakeoverBalances() {
        try {
            List<Takeoverbalance> takeoverbalances = takeoverBalanceService.findAll();

            if (takeoverbalances == null || takeoverbalances.isEmpty()) {
                log.info("No takeoverbalances found");
                return ResponseEntity.ok(List.of());
            }

            List<TakeoverBalanceDto> takeoverBalanceDtos = takeoverbalances.stream()
                    .map(balance -> TakeoverBalanceDto.builder()
                            .id(balance.getId())
                            .checkNumber(balance.getCheckNumber())
                            .loanNumber(balance.getLoanNumber())
                            .firstName(balance.getFirstName())
                            .lastName(balance.getLastName())
                            .middleName(balance.getMiddleName())
                            .voteName(balance.getVoteName())
                            .voteCode(balance.getVoteCode())
                            .deductionAmount(balance.getDeductionAmount())
                            .deductionBalance(balance.getDeductionBalance())
                            .paymentOption(balance.getPaymentOption())
                            .createdDate(balance.getCreatedDate())
                            .build()
                    )
                    .toList();

            log.info("Successfully fetched {} takeover balance", takeoverBalanceDtos.size());
            return ResponseEntity.ok(takeoverBalanceDtos);

        } catch (Exception e) {
            log.error("Error fetching takeover balance list", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(List.of());
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<Takeoverbalance>> searchTakeoverBalance(
            @RequestParam(required = false) String term) {
        return ResponseEntity.ok(takeoverBalanceService.searchTakeoverBalance(term));
    }

    @GetMapping("/{id}")
//    @CustomAnnotations.RequirePermission(Permission.MANAGE_USERS)
    public ResponseEntity<?> getTakaoverBalanceById(@PathVariable long id) throws Exception {
        try {
            Optional<Takeoverbalance> takeoverBalanceOption = takeoverBalanceService.findById(id);

            if (takeoverBalanceOption == null || takeoverBalanceOption.isEmpty()) {
                log.info("No balance found");
                return ResponseEntity.status(HttpStatus.OK).body("No balance found");
            }
            Takeoverbalance takeoverbalance = takeoverBalanceOption.get();

            TakeoverBalanceDto takeoverBalanceDto = TakeoverBalanceDto.builder()
                    .id(takeoverbalance.getId())
                    .checkNumber(takeoverbalance.getCheckNumber())
                    .loanNumber(takeoverbalance.getLoanNumber())
                    .firstName(takeoverbalance.getFirstName())
                    .lastName(takeoverbalance.getLastName())
                    .middleName(takeoverbalance.getMiddleName())
                    .voteName(takeoverbalance.getVoteName())
                    .voteCode(takeoverbalance.getVoteCode())
                    .deductionAmount(takeoverbalance.getDeductionAmount())
                    .deductionBalance(takeoverbalance.getDeductionBalance())
                    .paymentOption(takeoverbalance.getPaymentOption())
                    .createdDate(takeoverbalance.getCreatedDate())

                    .fspCode(takeoverbalance.getFspCode())
                    .fSPReferenceNumber(takeoverbalance.getFSPReferenceNumber())
                    .paymentReferenceNumber(takeoverbalance.getPaymentReferenceNumber())
                    .totalPayoffAmount(takeoverbalance.getTotalPayoffAmount())
                    .outstandingBalance(takeoverbalance.getOutstandingBalance())
                    .fspBankAccount(takeoverbalance.getFspBankAccount())
                    .fspBankAccountName(takeoverbalance.getFspBankAccountName())
                    .swiftCode(takeoverbalance.getSwiftCode())
                    .mnoChannels(takeoverbalance.getMnoChannels())
                    .finalPaymentDate(takeoverbalance.getFinalPaymentDate())
                    .lastDeductionDate(takeoverbalance.getLastDeductionDate())
                    .deductionEndDate(takeoverbalance.getDeductionEndDate())
                    .build();

            log.info("Successfully fetched takeover balance dto: {}", takeoverBalanceDto);
            return ResponseEntity.ok(takeoverBalanceDto);

        } catch (Exception e) {
            log.error("Error fetching charge: {}", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching charge :" + e.getMessage());
        }
    }

}
