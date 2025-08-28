package com.example.Project.Management.bayport.takeover.dto;

import com.example.Project.Management.bayport.common.enumerator.RequestType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TakeoverPaymentDto {
    private Long id;

    private String applicationNumber;

    private String loanNumber;

    private Long takeoverBalanceId;

    private String reason;

    private String fspReferenceNumber;

    private String paymentReferenceNumber;

    private BigDecimal totalPayoffAmount;

    private String paymentDate;

    private String paymentAdvice;

    private String paymentAdviceAttachment;

    private Date createdDate;

    private RequestType requestType;

    // balance
    private String balanceCheckNumber;

    private String balanceLoanNumber;

    private String balanceFspCode;

    private String balanceFSPReferenceNumber;

    private String balancePaymentReferenceNumber;

    private BigDecimal balanceTotalPayoffAmount;

    private BigDecimal balanceOutstandingBalance;

    private String balanceFspBankAccount;

    private String balanceFspBankAccountName;

    private String balanceSwiftCode;

    private String balanceMnoChannels;

    private String balanceFinalPaymentDate;

    private String balanceLastDeductionDate;

    private String balanceDeductionEndDate;

    private String balanceFirstName;

    private String balanceMiddleName;

    private String balanceLastName;

    private String balanceVoteCode;

    private String balanceVoteName;

    private BigDecimal balanceDeductionAmount;

    private String balanceDeductionCode;

    private String balanceDeductionName;

    private BigDecimal balanceDeductionBalance;

    private String balancePaymentOption;

    private Date balanceCreatedDate;
}
