package com.example.Project.Management.bayport.loan.dto.restructure.balance.request;

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
public class RestructureBalanceApiReqDetailDto {
    @XmlElement(name = "CheckNumber")
    private String checkNumber;

    @XmlElement(name = "LoanNumber")
    private String loanNumber;

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

    @XmlElement(name = "DeductionAmount")
    private BigDecimal deductionAmount;

    @XmlElement(name = "DeductionCode")
    private String deductionCode;

    @XmlElement(name = "DeductionName")
    private String deductionName;

    @XmlElement(name = "DeductionBalance")
    private BigDecimal deductionBalance;

    @XmlElement(name = "PaymentOption")
    private String paymentOption;
}
