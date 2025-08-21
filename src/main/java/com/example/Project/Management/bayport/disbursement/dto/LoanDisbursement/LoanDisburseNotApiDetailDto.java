package com.example.Project.Management.bayport.disbursement.dto.LoanDisbursement;

import jakarta.persistence.Column;
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
public class LoanDisburseNotApiDetailDto {

    @XmlElement(name = "ApplicationNumber")
    private String applicationNumber;

    @XmlElement(name = "Reason")
    private String reason;

    @XmlElement(name = "FSPReferenceNumber")
    private String fspReferenceNumber;

    @XmlElement(name = "LoanNumber")
    private String loanNumber;

    @XmlElement(name = "TotalAmountToPay")
    private BigDecimal totalAmountToPay;

    @XmlElement(name = "DisbursementDate")
    private String disbursementDate;

    @Column(columnDefinition = "TEXT")
    @XmlElement(name = "PaymentAdvice")
    private String paymentAdviceDto;

//    @XmlElement(name = "PaymentAdvice")
//    private String paymentAdvice;

    @XmlElement(name = "PaymentAdviceAttachment")
    private String paymentAdviceAttachment;


}
