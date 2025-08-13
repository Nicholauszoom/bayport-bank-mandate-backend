package com.example.Project.Management.bayport.loan.dto.takeover;

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
public class LoanTakeOverOfferApiReqDetailDto {

    @XmlElement(name = "CheckNumber")
    private String checkNumber;

    @XmlElement(name = "FirstName")
    private String firstName;

    @XmlElement(name = "MiddleName")
    private String middleName;

    @XmlElement(name = "LastName")
    private String lastName;

    @XmlElement(name = "VoteCode")
    private String voteCode;

    @XmlElement(name = "VoteName")
    private String voteName;

    @XmlElement(name = "NIN")
    private String nin;

    @XmlElement(name = "DesignationCode")
    private String designationCode;


    @XmlElement(name = "DesignationName")
    private String designationName;

    @XmlElement(name = "BasicSalary")
    private BigDecimal basicSalary = new BigDecimal("0.00") ;

    @XmlElement(name = "NetSalary")
    private BigDecimal netSalary = new BigDecimal("0.00");

    @XmlElement(name = "OneThirdAmount")
    private BigDecimal oneThirdAmount = new BigDecimal("0.00");

    @XmlElement(name = "DeductibleAmount")
    private BigDecimal DeductibleAmount = new BigDecimal("0.00");


    @XmlElement(name = "RetirementDate")
    private int retirementDate;

    @XmlElement(name = "TermsOfEmployment")
    private String termsOfEmployment;

    @XmlElement(name = "RequestedAmount")
    private BigDecimal requestedAmount = new BigDecimal("0.00");

    @XmlElement(name = "DesiredDeductibleAmount")
    private BigDecimal desiredDeductibleAmount = new BigDecimal("0.00");

    @XmlElement(name = "Tenure")
    private int tenure;

    @XmlElement(name = "ProductCode")
    private String productCode;

    @XmlElement(name = "InterestRate")
    private BigDecimal interestRate = new BigDecimal("0.00");

    @XmlElement(name = "ProcessingFee")
    private BigDecimal processingFee = new BigDecimal("0.00");

    @XmlElement(name = "Insurance")
    private BigDecimal insurance = new BigDecimal("0.00");

    @XmlElement(name = "FSP1Code")
    private String fspOneCode;

    @XmlElement(name = "FSP1LoanNumber")
    private String fspOneLoanNumber;

    @XmlElement(name = "TakeOverBalance")
    private BigDecimal takeOverBalance = new BigDecimal("0.00");

    @XmlElement(name = "FSP1EndDate")
    private Date fspOneEndDate;

    @XmlElement(name = "FSP1LastDeductionDate")
    private Date fspOneLastDeductionDate ;

    @XmlElement(name = "FSP1BankAccount")
    private String fspOneBankAccount;

    @XmlElement(name = "FSP1BankAccountName")
    private String fspOneBankAccountName;

    @XmlElement(name = "FSP1SWIFTCode")
    private String fspOneSWIFTCode;

    @XmlElement(name = "FSP1MNOChannels")
    private String fspOneMNOChannels;

    @XmlElement(name = "FSP1PaymentReferenceNumber")
    private String fspOnePaymentReferenceNumber;

    @XmlElement(name = "FSP1FinalPaymentDate")
    private Date fspOneFinalPaymentDate ;

    @XmlElement(name = "ApplicationNumber")
    private String applicationNumber;




}
