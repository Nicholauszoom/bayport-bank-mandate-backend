package com.example.Project.Management.bayport.borrower.dto.AccountValidationResp;

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
public class AccountValidationApiRespDto {
    @XmlElement(name = "Data")
    private AccountValidationApiRespDataDto accountValidationApiRespDataDto;

    @XmlElement(name = "Signature")
    private String messageHash;
}
