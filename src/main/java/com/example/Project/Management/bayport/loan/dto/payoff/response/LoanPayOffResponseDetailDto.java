package com.example.Project.Management.bayport.loan.dto.payoff.response;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@XmlRootElement(name = "MessageDetails")
@XmlAccessorType(XmlAccessType.FIELD)
public class LoanPayOffResponseDetailDto {

    @XmlElement(name = "LoanNumber")
    private String loanNumber;

    @XmlElement(name = "FSPReferenceNumber")
    private String fspReferenceNumber;

    @XmlElement(name = "paymentReferenceNumber")
    private String paymentReferenceNumber;

    @XmlElement(name = "TotalPayoffAmount")
    private BigDecimal totalPayoffAmount;

    @XmlElement(name = "OutstandingBalance")
    private BigDecimal outstandingBalance;

    @XmlElement(name = "FinalPaymentDate")
    private Date finalPaymentDate;

    @XmlElement(name = "LastDeductionDate")
    private LocalDate lastDeductionDate;

    @XmlElement(name = "LastPayDate")
    private Date lastPayDate;

    @XmlElement(name = "EndDate")
    private Date endDate;

}
