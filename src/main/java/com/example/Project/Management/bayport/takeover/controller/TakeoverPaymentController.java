package com.example.Project.Management.bayport.takeover.controller;

import com.example.Project.Management.bayport.common.enumerator.PaymentStatus;
import com.example.Project.Management.bayport.common.enumerator.RequestAction;
import com.example.Project.Management.bayport.common.enumerator.RequestType;
import com.example.Project.Management.bayport.loan.service.LoanService;
import com.example.Project.Management.bayport.takeover.dto.TakeoverPaymentDto;
import com.example.Project.Management.bayport.takeover.dto.portal.PaymentAcknoledgeDto;
import com.example.Project.Management.bayport.takeover.entity.Takeoverbalance;
import com.example.Project.Management.bayport.takeover.entity.Takeoverpayment;
import com.example.Project.Management.bayport.takeover.service.TakeoverBalanceService;
import com.example.Project.Management.bayport.takeover.service.TakeoverPaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/takeover/payment")
@Slf4j
public class TakeoverPaymentController {
    private final TakeoverPaymentService takeoverPaymentService;
    private final TakeoverBalanceService takeoverBalanceService;
    private final LoanService loanService;

    public TakeoverPaymentController(TakeoverPaymentService takeoverPaymentService, TakeoverBalanceService takeoverBalanceService, LoanService loanService) {
        this.takeoverPaymentService = takeoverPaymentService;
        this.takeoverBalanceService = takeoverBalanceService;
        this.loanService = loanService;
    }


    @GetMapping("/list")
//@CustomAnnotations.RequirePermission(Permission.MANAGE_USERS)
    public ResponseEntity<List<TakeoverPaymentDto>> getTakeoverPayments(@RequestParam(required = false) RequestType requestType) {
        try {
            List<Takeoverpayment> takeoverpayments = takeoverPaymentService.findAll(requestType);

            if (takeoverpayments == null || takeoverpayments.isEmpty()) {
                log.info("No takeover payment found");
                return ResponseEntity.ok(List.of());
            }

            List<TakeoverPaymentDto> takeoverPaymentDtos = takeoverpayments.stream()
                    .map(payment -> TakeoverPaymentDto.builder()
                            .id(payment.getId())
                            .applicationNumber(payment.getApplicationNumber())
                            .loanNumber(payment.getLoanNumber())
                            .reason(payment.getReason())
                            .fspReferenceNumber(payment.getFspReferenceNumber())
                            .paymentReferenceNumber(payment.getPaymentReferenceNumber())
                            .totalPayoffAmount(payment.getTotalPayoffAmount())
                            .paymentDate(payment.getPaymentDate())
                            .paymentAdvice(payment.getPaymentAdvice())
                            .paymentAdviceAttachment(payment.getPaymentAdviceAttachment())
                            .createdDate(payment.getCreatedDate())
                            .build()
                    )
                    .toList();

            log.info("Successfully fetched {} takeover payment", takeoverPaymentDtos.size());
            return ResponseEntity.ok(takeoverPaymentDtos);

        } catch (Exception e) {
            log.error("Error fetching takeover payment list", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(List.of());
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<Takeoverpayment>> searchTakeoverPayment(
            @RequestParam(required = false) String term) {
        return ResponseEntity.ok(takeoverPaymentService.searchTakeoverPayment(term));
    }

    @GetMapping("/{id}")
//    @CustomAnnotations.RequirePermission(Permission.MANAGE_USERS)
    public ResponseEntity<?> getTakaoverPaymentById(@PathVariable long id) throws Exception {
        try {
            Optional<Takeoverpayment> takeoverPaymentOption = takeoverPaymentService.findById(id);

            if (takeoverPaymentOption == null || takeoverPaymentOption.isEmpty()) {
                log.info("No payment found");
                return ResponseEntity.status(HttpStatus.OK).body("No payment found");
            }
            Takeoverpayment takeoverpayment = takeoverPaymentOption.get();

            Optional<Takeoverbalance> takeoverbalanceOpt = takeoverBalanceService.findById(takeoverpayment.getTakeoverBalanceId());

            if(takeoverbalanceOpt == null || takeoverbalanceOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No takeoverbalance found");
            }
            Takeoverbalance takeoverbalance = takeoverbalanceOpt.get();

            TakeoverPaymentDto takeoverPaymentDto = TakeoverPaymentDto.builder()
                    .id(takeoverpayment.getId())
                    .applicationNumber(takeoverpayment.getApplicationNumber())
                    .loanNumber(takeoverpayment.getLoanNumber())
                    .reason(takeoverpayment.getReason())
                    .fspReferenceNumber(takeoverpayment.getFspReferenceNumber())
                    .paymentReferenceNumber(takeoverpayment.getPaymentReferenceNumber())
                    .totalPayoffAmount(takeoverpayment.getTotalPayoffAmount())
                    .paymentDate(takeoverpayment.getPaymentDate())
                    .paymentAdvice(takeoverpayment.getPaymentAdvice())
                    .paymentAdviceAttachment(takeoverpayment.getPaymentAdviceAttachment())
                    .createdDate(takeoverpayment.getCreatedDate())
                    .requestType(takeoverpayment.getRequestType())

                    .balanceCheckNumber(takeoverbalance.getCheckNumber())
                    .balanceLoanNumber(takeoverbalance.getLoanNumber())
                    .balanceFirstName(takeoverbalance.getFirstName())
                    .balanceLastName(takeoverbalance.getLastName())
                    .balanceMiddleName(takeoverbalance.getMiddleName())
                    .balanceVoteName(takeoverbalance.getVoteName())
                    .balanceVoteCode(takeoverbalance.getVoteCode())
                    .balanceDeductionAmount(takeoverbalance.getDeductionAmount())
                    .balanceDeductionBalance(takeoverbalance.getDeductionBalance())
                    .balancePaymentOption(takeoverbalance.getPaymentOption())
                    .balanceCreatedDate(takeoverbalance.getCreatedDate())

                    .balanceFspCode(takeoverbalance.getFspCode())
                    .balanceFSPReferenceNumber(takeoverbalance.getFSPReferenceNumber())
                    .balancePaymentReferenceNumber(takeoverbalance.getPaymentReferenceNumber())
                    .balanceTotalPayoffAmount(takeoverbalance.getTotalPayoffAmount())
                    .balanceOutstandingBalance(takeoverbalance.getOutstandingBalance())
                    .balanceFspBankAccount(takeoverbalance.getFspBankAccount())
                    .balanceFspBankAccountName(takeoverbalance.getFspBankAccountName())
                    .balanceSwiftCode(takeoverbalance.getSwiftCode())
                    .balanceMnoChannels(takeoverbalance.getMnoChannels())
                    .balanceFinalPaymentDate(takeoverbalance.getFinalPaymentDate())
                    .balanceLastDeductionDate(takeoverbalance.getLastDeductionDate())
                    .balanceDeductionEndDate(takeoverbalance.getDeductionEndDate())

                    .build();

            log.info("Successfully fetched takeover Payment: {}", takeoverPaymentDto);
            return ResponseEntity.ok(takeoverPaymentDto);

        } catch (Exception e) {
            log.error("Error fetching takeover Payment: {}", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching takeover Payment :" + e.getMessage());
        }
    }

    @PostMapping("/{id}/acknoledge")
    public ResponseEntity<?> loanApprove(@PathVariable Long id,
                                         @RequestBody PaymentAcknoledgeDto paymentAcknoledgeDto) throws Exception {
        return loanService.paymentAcknoledgementNotification(id,paymentAcknoledgeDto);
    }
}
