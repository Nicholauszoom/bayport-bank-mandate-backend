package com.example.Project.Management.bayport.loan.dto.portal;

import com.example.Project.Management.bayport.common.enumerator.LoanType;
import com.example.Project.Management.bayport.common.enumerator.RequestType;
import jakarta.persistence.Column;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoanDto {
    private Long id;
    private String firstName;
    private String middleName;
    private String lastName;
    private String sex;
    private String bankAccountNumber;
    private Date employmentDate;
    private String maritalStatus;
    private Date confirmationDate;
    private BigDecimal totalEmployeeDeduction;
    private String nearestBranchName;
    private String nearestBranchCode;
    private String voteCode;
    private String voteName;
    private String NIN;
    private String designationCode;
    private String designationName;
    private BigDecimal basicSalary;
    private BigDecimal netSalary;
    private BigDecimal oneThirdAmount;
    private BigDecimal requestedAmount;
    private BigDecimal desiredDeductibleAmount;
    private int retirementDate;
    private String termsOfEmployment;
    private int tenure;
    private String productCode;
    private BigDecimal interestRate;
    private BigDecimal processingFee;
    private BigDecimal insurance;
    private String physicalAddress;
    private String emailAddress;
    private String mobileNumber;
    private String applicationNumber;
    private String loanPurpose;
    private Date contractStartDate;
    private Date contractEndDate;
    private String checkNumber;
    private String swiftCode;
    private String funding;
    private LoanType loanType;
    private Date createdDate;
    private RequestType requestType;
    private String essApprovalReason;
    private String reason;
    private String disbursementDate;
    private String initialApproveDate;
    private String hroApproveDate;
    private String employeeCancellDate;
    private String liquidationDate;
    private String remark;

    // Charge Response
    private BigDecimal totalInsurance;
    private BigDecimal totalProcessingFee;
    private BigDecimal totalInterestRateAmount;
    private BigDecimal otherCharges;
    private BigDecimal netLoanAmount;
    private BigDecimal totalAmountToPay;
    private BigDecimal eligibleAmount;
    private BigDecimal monthlyReturnAmount;
    private int responseTenure;
    private BigDecimal takeHomeAmount;
    private BigDecimal insuranceProcessFee;

}
