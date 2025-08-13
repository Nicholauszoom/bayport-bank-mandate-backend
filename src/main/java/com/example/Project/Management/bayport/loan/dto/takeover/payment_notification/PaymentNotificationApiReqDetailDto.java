package com.example.Project.Management.bayport.loan.dto.takeover.payment_notification;

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
public class PaymentNotificationApiReqDetailDto {

    @XmlElement(name = "ApplicationNumber")
    private String applicationNumber;

    @XmlElement(name = "LoanNumber")
    private String loanNumber;

    @XmlElement(name = "Reason")
    private String reason;

    @XmlElement(name = "FSPReferenceNumber")
    private String fspReferenceNumber;

    @XmlElement(name = "PaymentReferenceNumber")
    private String paymentReferenceNumber;

    @XmlElement(name = "TotalPayoffAmount")
    private BigDecimal totalPayoffAmount;

    @XmlElement(name = "PaymentDate")
    private String paymentDate;

    @Column(columnDefinition = "TEXT")
    @XmlElement(name = "PaymentAdvice")
    private String paymentAdvice;

    @XmlElement(name = "PaymentAdviceAttachment")
    private String paymentAdviceAttachment;

}
