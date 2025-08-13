package com.dflex.emkopo.app.borrower.dto.AccountValidationResp;

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
@XmlRootElement(name = "MessageDetails")
@XmlAccessorType(XmlAccessType.FIELD)
public class AccountValidationApiRespDetailDto {

    @XmlElement(name = "Valid")
    private Boolean valid;

    @XmlElement(name = "Reason")
    private String reason;


}
