package com.example.Project.Management.bayport.balance.controller;

import com.example.Project.Management.bayport.balance.dto.portal.BalanceDto;
import com.example.Project.Management.bayport.balance.entity.Balance;
import com.example.Project.Management.bayport.balance.service.BalanceService;
import com.example.Project.Management.bayport.charge.dto.portal.ChargeDto;
import com.example.Project.Management.bayport.charge.entity.Charge;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/balance")
@RequiredArgsConstructor
@Slf4j
public class BalanceController {

    private final BalanceService balanceService;

    @GetMapping("/list")
    public ResponseEntity<List<BalanceDto>> getBalanceList() {
        try {
            List<Balance> balances = balanceService.findAll();

            if (balances == null || balances.isEmpty()) {
                log.info("No balances found");
                return ResponseEntity.ok(List.of());
            }

            List<BalanceDto> balanceDtos = balances.stream()
                    .map(balance -> BalanceDto.builder()
                            .id(balance.getId())
                            // Request fields
                            .checkNumber(balance.getCheckNumber())
                            .loanNumber(balance.getLoanNumber())
                            .firstName(balance.getFirstName())
                            .middleName(balance.getMiddleName())
                            .lastName(balance.getLastName())
                            .voteCode(balance.getVoteCode())
                            .voteName(balance.getVoteName())
                            .deductionAmount(balance.getDeductionAmount())
                            .deductionCode(balance.getDeductionCode())
                            .deductionName(balance.getDeductionName())
                            .deductionBalance(balance.getDeductionBalance())
                            .paymentOption(balance.getPaymentOption())
                            // Response fields
                            .fSPReferenceNumber(balance.getFSPReferenceNumber())
                            .paymentReferenceNumber(balance.getPaymentReferenceNumber())
                            .totalPayoffAmount(balance.getTotalPayoffAmount())
                            .outstandingBalance(balance.getOutstandingBalance())
                            .finalPaymentDate(balance.getFinalPaymentDate())
                            .lastDeductionDate(balance.getLastDeductionDate())
                            .lastPayDate(balance.getLastPayDate())
                            .endDate(balance.getEndDate())
                            .build()
                    )
                    .toList();

            log.info("Successfully fetched {} balances", balanceDtos.size());
            return ResponseEntity.ok(balanceDtos);

        } catch (Exception e) {
            log.error("Error fetching balance list", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(List.of());
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<BalanceDto>> searchBalances(
            @RequestParam(required = false) String term) {
        return ResponseEntity.ok(balanceService.searchBalances(term));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBalanceById(@PathVariable long id) {
        try {
            Optional<Balance> balanceOption = balanceService.findById(id);

            if (balanceOption.isEmpty()) {
                log.info("No balance found with id: {}", id);
                return ResponseEntity.status(HttpStatus.OK).body("No balance found");
            }
            Balance balance = balanceOption.get();

            BalanceDto balanceDto = BalanceDto.builder()
                    .id(balance.getId())
                    // Request fields
                    .checkNumber(balance.getCheckNumber())
                    .loanNumber(balance.getLoanNumber())
                    .firstName(balance.getFirstName())
                    .middleName(balance.getMiddleName())
                    .lastName(balance.getLastName())
                    .voteCode(balance.getVoteCode())
                    .voteName(balance.getVoteName())
                    .deductionAmount(balance.getDeductionAmount())
                    .deductionCode(balance.getDeductionCode())
                    .deductionName(balance.getDeductionName())
                    .deductionBalance(balance.getDeductionBalance())
                    .paymentOption(balance.getPaymentOption())
                    .createdDate(balance.getCreatedDate())
                    // Response fields
                    .fSPReferenceNumber(balance.getFSPReferenceNumber())
                    .paymentReferenceNumber(balance.getPaymentReferenceNumber())
                    .totalPayoffAmount(balance.getTotalPayoffAmount())
                    .outstandingBalance(balance.getOutstandingBalance())
                    .finalPaymentDate(balance.getFinalPaymentDate())
                    .lastDeductionDate(balance.getLastDeductionDate())
                    .lastPayDate(balance.getLastPayDate())
                    .endDate(balance.getEndDate())
                    .build();

            log.info("Successfully fetched balance: {}", balanceDto);
            return ResponseEntity.ok(balanceDto);

        } catch (Exception e) {
            log.error("Error fetching balance: {}", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching balance: " + e.getMessage());
        }
    }

    @PostMapping("/{id}/delete")
    public void deleteBalance(@PathVariable("id") long id) {
        balanceService.deleteBalance(id);
    }
}
