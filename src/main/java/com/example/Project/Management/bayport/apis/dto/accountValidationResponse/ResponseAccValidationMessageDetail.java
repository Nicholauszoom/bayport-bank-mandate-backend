package com.dflex.emkopo.api.dto.accountValidationResponse;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
public class ResponseAccValidationMessageDetail {

    @XmlElement(name = "Valid")
    private Boolean valid;

    @XmlElement(name = "Reason")
    private String reason;
}
