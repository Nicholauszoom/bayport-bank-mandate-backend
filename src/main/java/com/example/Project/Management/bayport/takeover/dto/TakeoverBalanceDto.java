package com.example.Project.Management.bayport.takeover.dto;

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
public class TakeoverBalanceDto {
    private Long id;

    private String checkNumber;

    private String loanNumber;

    private String fspCode;

    private String fSPReferenceNumber;

    private String paymentReferenceNumber;

    private BigDecimal totalPayoffAmount;

    private BigDecimal outstandingBalance;

    private String fspBankAccount;

    private String fspBankAccountName;

    private String swiftCode;

    private String mnoChannels;

    private String finalPaymentDate;

    private String lastDeductionDate;

    private String deductionEndDate;

    // request
    private String firstName;

    private String middleName;

    private String lastName;

    private String voteCode;

    private String voteName;

    private BigDecimal deductionAmount;

    private String deductionCode;

    private String deductionName;

    private BigDecimal deductionBalance;

    private String paymentOption;

    private Date createdDate;
}
