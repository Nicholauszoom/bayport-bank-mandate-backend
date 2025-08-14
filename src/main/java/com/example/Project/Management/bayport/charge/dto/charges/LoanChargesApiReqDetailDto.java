package com.example.Project.Management.bayport.loan.dto.charges;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@XmlRootElement(name = "MessageDetails")
@XmlAccessorType(XmlAccessType.FIELD)
public class LoanChargesApiReqDetailDto {

    @XmlElement(name = "CheckNumber")
    private String checkNumber;

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

    @XmlElement(name = "DeductibleAmount")
    private BigDecimal deductibleAmount;

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

    @XmlElement(name = "VoteCode")
    private String voteCode;

    @XmlElement(name = "TotalEmployeeDeduction")
    private BigDecimal totalEmployeeDeduction;

    @XmlElement(name = "JobClassCode")
    private String jobClassCode;
}
