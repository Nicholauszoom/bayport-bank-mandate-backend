package com.example.Project.Management.bayport.loan.dto.registration;

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
public class LoanRegistrationApiReqDto {

    @XmlElement(name = "Data")
    private LoanRegistrationApiReqDataDto loanRegistrationData;

    @XmlElement(name = "Signature")
    private String messageHash;
}
