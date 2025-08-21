package com.example.Project.Management.bayport.loan.dto.registration;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@XmlRootElement(name = "MessageDetails")
@XmlAccessorType(XmlAccessType.FIELD)
public class LoanRegistrationReqDetailDto {
    @XmlElement(name = "CheckNumber")
    private String checkNumber;

    @XmlElement(name = "FirstName")
    private String firstName;

    @XmlElement(name = "MiddleName")
    private String middleName;

    @XmlElement(name = "LastName")
    private String lastName;

    @XmlElement(name = "Sex")
    private String sex;

    @XmlElement(name = "BankAccountNumber")
    private String bankAccountNumber;

    @XmlElement(name = "EmploymentDate")
    private Date employmentDate;

    @XmlElement(name = "MaritalStatus")
    private String maritalStatus;

    @XmlElement(name = "ConfirmationDate")
    private Date confirmationDate;

    @XmlElement(name = "TotalEmployeeDeduction")
    private BigDecimal totalEmployeeDeduction;

    @XmlElement(name = "NearestBranchName")
    private String nearestBranchName;

    @XmlElement(name = "NearestBranchCode")
    private String nearestBranchCode;

    @XmlElement(name = "VoteCode")
    private String voteCode;

    @XmlElement(name = "VoteName")
    private String voteName;

    @XmlElement(name = "NIN")
    private String NIN;

    @XmlElement(name = "DesignationCode")
    private String designationCode;

    @XmlElement(name = "DesignationName")
    private String designationName;

    @XmlElement(name = "BasicSalary")
    private BigDecimal basicSalary;

    @XmlElement(name = "NetSalary")
    private BigDecimal netSalary;

    @XmlElement(name = "OneThirdAmount")
    private BigDecimal oneThirdAmount;

    @XmlElement(name = "RequestedAmount")
    private BigDecimal requestedAmount;

    @XmlElement(name = "DesiredDeductibleAmount")
    private BigDecimal desiredDeductibleAmount;

    @XmlElement(name = "RetirementDate")
    private int retirementDate;

    @XmlElement(name = "TermsOfEmployment")
    private String termsOfEmployment;

    @XmlElement(name = "Tenure")
    private int tenure;

    @XmlElement(name = "ProductCode")
    private String productCode;

    @XmlElement(name = "InterestRate")
    private BigDecimal interestRate;

    @XmlElement(name = "ProcessingFee")
    private BigDecimal processingFee;

    @XmlElement(name = "Insurance")
    private BigDecimal insurance;

    @XmlElement(name = "PhysicalAddress")
    private String physicalAddress;

    @XmlElement(name = "EmailAddress")
    private String emailAddress;

    @XmlElement(name = "MobileNumber")
    private String mobileNumber;

    @XmlElement(name = "ApplicationNumber")
    private String applicationNumber;

    @XmlElement(name = "LoanPurpose")
    private String loanPurpose;

    @XmlElement(name = "ContractStartDate")
    private Date contractStartDate;

    @XmlElement(name = "ContractEndDate")
    private Date contractEndDate;

    @XmlElement(name = "SwiftCode")
    private String swiftCode;

    @XmlElement(name = "Funding")
    private String funding;

}
