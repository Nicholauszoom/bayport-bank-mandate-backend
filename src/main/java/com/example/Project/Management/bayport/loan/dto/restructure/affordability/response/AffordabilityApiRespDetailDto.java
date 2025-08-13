package com.example.Project.Management.bayport.loan.dto.restructure.affordability.response;

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
public class AffordabilityApiRespDetailDto {
    @XmlElement(name = "DesiredDeductibleAmount")
    private BigDecimal desiredDeductibleAmount;
    @XmlElement(name = "TotalInsurance")
    private BigDecimal totalInsurance;
    @XmlElement(name = "TotalProcessingFees")
    private BigDecimal totalProcessingFee;
    @XmlElement(name = "TotalInterestRateAmount")
    private BigDecimal totalInterestRateAmount;
    @XmlElement(name = "OtherCharges")
    private BigDecimal otherCharges;
    @XmlElement(name = "NetLoanAmount")
    private BigDecimal netLoanAmount;
    @XmlElement(name = "TotalAmountToPay")
    private BigDecimal totalAmountToPay;
    @XmlElement(name = "Tenure")
    private int tenure;
    @XmlElement(name = "EligibleAmount")
    private BigDecimal eligibleAmount;
    @XmlElement(name = "MonthlyReturnAmount")
    private BigDecimal monthlyReturnAmount;
}
