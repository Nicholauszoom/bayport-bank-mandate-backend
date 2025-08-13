package com.dflex.emkopo.api.dto.accountValidationResponse;


import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement(name = "Document")
@XmlAccessorType(XmlAccessType.FIELD)
public class MessageAccValidationResponseWrapper {

    @XmlElement(name = "Data")
    private MessageAccValidationResponse data;

    @XmlElement(name = "Signature")
    private String signature;
}
