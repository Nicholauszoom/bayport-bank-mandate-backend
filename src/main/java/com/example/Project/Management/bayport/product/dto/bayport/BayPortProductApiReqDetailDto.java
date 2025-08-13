package com.example.Project.Management.bayport.product.dto.bayport;

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
public class BayPortProductApiReqDetailDto {

    @XmlElement(name = "ProductCode")
    private String productCode;

    @XmlElement(name = "Name")
    private String name;

    @XmlElement(name = "MinimumTenure")
    private Integer minimumTenure;

    @XmlElement(name = "MaximumTenure")
    private Integer maximumTenure;

    @XmlElement(name = "MonthlyRate")
    private Double monthlyRate;

    @XmlElement(name = "AdminFee")
    private BigDecimal adminFee;

    @XmlElement(name = "CreditLife")
    private BigDecimal creditLife;

    @XmlElement(name = "MinimumPrincipal")
    private BigDecimal minimumPrincipal;

    @XmlElement(name = "MaximumPrincipal")
    private BigDecimal maximumPrincipal ;

    @XmlElement(name = "VatRate")
    private BigDecimal vatRate ;

    @XmlElement(name = "PrincipalIncrement")
    private BigDecimal principalIncrement;

    @XmlElement(name = "AnnualPercentageRate")
    private BigDecimal annualPercentageRate;

    @XmlElement(name = "RepaymentType")
    private String repaymentType;

    @XmlElement(name = "OtherCharges")
    private BigDecimal otherCharges;

}