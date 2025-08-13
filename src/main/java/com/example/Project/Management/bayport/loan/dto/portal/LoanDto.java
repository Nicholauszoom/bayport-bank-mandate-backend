package com.example.Project.Management.bayport.loan.dto.portal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class LoanDto {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String loanNumber;

    private String checkNumber;

    private BigDecimal settlementAmount = new BigDecimal("0.00");

    private BigDecimal loanAmount= new BigDecimal("0.00");

    private String loanType;

    private Date loanDate;

    private Date fspOneLastDeductionDate;

    private Date fspOneFinalPaymentDate;

    private String repaymentType;

    private String fsp2Code;

    private String fspTwoProductCode;

    private int minTenure;

    private int maxTenure;

    private String applicationNumber;

    private BigDecimal interest= new BigDecimal("0.00");

    private BigDecimal insurance= new BigDecimal("0.00");

    private BigDecimal processingFee= new BigDecimal("0.00");

    private int tenure;

    private String deductionCode;

    private String nearestBranchName;

    private String nearestBranchCode;

    private String productCode;

    private String fspCode;

    private String loanPurpose;

    private String bankAccountNumber;

    private String bankAccountName;

    private String deductionName;

    private BigDecimal deductibleAmount = new BigDecimal("0.00");

    private String fspReferenceNumber;

    private BigDecimal desiredDeductibleAmount = new BigDecimal("0.00");

    private BigDecimal requestedAmount = new BigDecimal("0.00");

    private BigDecimal deductionBalance = new BigDecimal("0.00");

    private BigDecimal totalAmountToPay = new BigDecimal("0.00");

    private String comment;

    private String swiftCode;

    private String mnoChannels;

    private String paymentReferenceNumber;

    private String loanStatus;

    private String funding;

    private BigInteger borrowerId;

    private String borrower;

}
