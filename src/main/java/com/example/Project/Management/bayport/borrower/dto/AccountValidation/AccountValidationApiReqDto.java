package com.example.Project.Management.bayport.borrower.dto.AccountValidation;

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
