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
@XmlRootElement(name = "Document")
@XmlAccessorType(XmlAccessType.FIELD)
public class LoanDisburseNotApiDto {
    @XmlElement(name = "Data")
    private LoanDisburseNotApiDataDto loanDisburseNotApiData;

    @XmlElement(name = "Signature")
    private String messageHash;
}
