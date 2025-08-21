package com.example.Project.Management.bayport.charge.entity;

import com.example.Project.Management.bayport.common.entity.CommonEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "charge")
@Builder
public class Charge extends CommonEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "charge_id_generator")
    @SequenceGenerator(name = "charge_id_generator", sequenceName = "seq_charge", initialValue = 1, allocationSize = 1)
    @Column(name = "id", nullable = false, precision = 11)
    private Long id;

    @Column(name = "check_number", nullable = false)
    private String checkNumber;

    @Column(name = "designation_code")
    private String designationCode;

    @Column(name = "designation_name")
    private String designationName;

    @Column(name = "basic_salary", precision = 15, scale = 2)
    private BigDecimal basicSalary;

    @Column(name = "net_salary", precision = 15, scale = 2)
    private BigDecimal netSalary;

    @Column(name = "one_third_amount", precision = 15, scale = 2)
    private BigDecimal oneThirdAmount;

    @Column(name = "requested_amount", precision = 15, scale = 2)
    private BigDecimal requestedAmount;

    @Column(name = "deductible_amount", precision = 15, scale = 2)
    private BigDecimal deductibleAmount;

    @Column(name = "desired_deductible_amount", precision = 15, scale = 2)
    private BigDecimal desiredDeductibleAmount;

    @Column(name = "retirement_date")
    private int retirementDate;

    @Column(name = "terms_of_employment")
    private String termsOfEmployment;

    @Column(name = "tenure")
    private int tenure;

    @Column(name = "response_tenure")
    private int responseTenure;

    @Column(name = "product_code")
    private String productCode;

    @Column(name = "vote_code")
    private String voteCode;

    @Column(name = "total_employee_deduction", precision = 15, scale = 2)
    private BigDecimal totalEmployeeDeduction;

    @Column(name = "job_class_code")
    private String jobClassCode;

    @Column(name = "total_insurance")
    private BigDecimal totalInsurance;

    @Column(name = "total_processing_fee")
    private BigDecimal totalProcessingFee;

    @Column(name = "total_interest_rate_amount")
    private BigDecimal totalInterestRateAmount;

    @Column(name = "other_charges")
    private BigDecimal otherCharges;

    @Column(name = "take_home_amount")
    private BigDecimal takeHomeAmount;

    @Column(name = "net_loan_amount")
    private BigDecimal netLoanAmount;

    @Column(name = "total_amount_to_pay")
    private BigDecimal totalAmountToPay;

    @Column(name = "eligible_amount")
    private BigDecimal eligibleAmount;

    @Column(name = "monthly_return_amount")
    private BigDecimal monthlyReturnAmount;

    @Column(name = "insurance_processing_fee")
    private BigDecimal insuranceProcessFee;

}
