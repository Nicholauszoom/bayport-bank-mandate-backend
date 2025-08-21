package com.example.Project.Management.bayport.borrower.entity;

import com.example.Project.Management.bayport.fsp.models.entities.FinancialServiceProvider;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name = "tab_borrower")
public class Borrower {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "borrower_id_generator")
    @SequenceGenerator(name = "borrower_id_generator", sequenceName = "seq_borrower", initialValue = 1, allocationSize = 1)
    @Column(name = "id", nullable = false, precision = 11)
    private Long id;

    @Column(name = "first_name", length = 255, nullable = true)
    private String firstName;

    @Column(name = "middle_name", length = 255, nullable = true)
    private String middleName;

    @Column(name = "last_name", length = 255, nullable = true)
    private String lastName;


    @Column(name = "full_name", length = 255, nullable = true)
    private String fullName;


    @Column(name = "sex", length = 255, nullable = true)
    private String sex;

    @Column(name = "employment_date", length = 255, nullable = true)
    private Date employmentDate;

    @Column(name = "marital_status", length = 255, nullable = true)
    private String maritalStatus;

    @Column(name = "confirmation_date", length = 255, nullable = true)
    private Date confirmationDate;

    @Column(name = "total_employee_deduction", length = 255,nullable = true)
    private BigDecimal totalEmployeeDeduction;

    @Column(name = "nearest_branch_name", length = 255, nullable = true)
    private String nearestBranchName;

    @Column(name = "vote_code", length = 255, nullable = true)
    private String voteCode;

    @Column(name = "vote_name", length = 255, nullable = true)
    private String voteName;

    @Column(name = "nin", length = 255, nullable = true)
    private String NIN;

    @Column(name = "designation_code", length = 255, nullable = true)
    private String designationCode;

    @Column(name = "designation_name", length = 255, nullable = true)
    private String designationName;

    @Column(name = "bank_account_number", length = 255, nullable = true)
    private String bankAccountNumber;

    @Column(name = "basic_salary", length = 255, nullable = true)
    private BigDecimal basicSalary;

    @Column(name = "net_salary", length = 255, nullable = true)
    private BigDecimal netSalary;

    @Column(name = "one_third_amount", length = 255, nullable = true)
    private BigDecimal oneThirdAmount;

    @Column(name = "retirement_date", length = 255, nullable = true)
    private int retirementDate;

    @Column(name = "terms_of_employment", length = 255, nullable = true)
    private String termsOfEmployment;

    @Column(name = "interest_rate", length = 255, nullable = true)
    private double iterestRate;

    @Column(name = "physical_address", length = 255, nullable = true)
    private String physicalAddress;

    @Column(name = "email_address", length = 255, nullable = true)
    private String emailAddress;

    @Column(name = "check_number", length = 255, nullable = true)
    private String checkNumber;

    @Column(name = "mobile_number", length = 255, nullable = true)
    private String mobileNumber;

    @Column(name = "fsp_code", length = 255, nullable = false)
    private String fspCode;

    @Column(name = "contract_start_date", length = 255, nullable = true)
    private Date contractStartDate;

    @Column(name = "contract_end_date", length = 255, nullable = true)
    private Date contractEndDate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "fsp_id", nullable = false)
    private FinancialServiceProvider financialServiceProvider;



}
