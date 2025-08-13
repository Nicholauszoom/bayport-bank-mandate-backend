package com.dflex.emkopo.app.borrower.dto.AccountValidation;

import com.dflex.emkopo.app.loan.dto.registration.LoanRegistrationApiReqDataDto;
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
public class AccountValidationApiReqDto {
    @XmlElement(name = "Data")
    private AccountValidationApiReqDataDto accountValidationApiReqDataDto;

    @XmlElement(name = "Signature")
    private String messageHash;
}
