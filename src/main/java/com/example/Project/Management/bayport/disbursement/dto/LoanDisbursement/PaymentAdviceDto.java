package com.dflex.emkopo.app.disbursement.dto.LoanDisbursement;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@XmlRootElement(name = "PaymentAdvice")
@XmlAccessorType(XmlAccessType.FIELD)
public class PaymentAdviceDto {

    @XmlElement(name = "AdvicePayment")
    private String advicePayment;
}
