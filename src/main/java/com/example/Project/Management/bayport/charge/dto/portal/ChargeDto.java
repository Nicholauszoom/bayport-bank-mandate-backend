package com.example.Project.Management.bayport.charge.dto.portal;

import jakarta.xml.bind.annotation.XmlElement;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

@Data
@Builder
public class ChargeDto {
    // Request
    private Long id;
    private String checkNumber;
    private String designationCode;
    private String designationName;
    private BigDecimal basicSalary;
    private BigDecimal netSalary;
    private BigDecimal oneThirdAmount;
    private BigDecimal requestedAmount;
    private BigDecimal deductibleAmount;
    private BigDecimal desiredDeductibleAmount;
    private Integer retirementDate;
    private String termsOfEmployment;
    private Integer tenure;
    private String productCode;
    private String voteCode;
    private BigDecimal totalEmployeeDeduction;
    private String jobClassCode;
    private Date createdDate;

    // Response
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