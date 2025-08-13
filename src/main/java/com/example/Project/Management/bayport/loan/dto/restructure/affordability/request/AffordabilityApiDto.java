package com.example.Project.Management.bayport.loan.dto.restructure.affordability.request;

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
public class AffordabilityApiDto {
    @XmlElement(name = "Data")
    private AffordabilityApiReqDataDto affordabilityApiReqDataDto;

    @XmlElement(name = "Signature")
    private String messageHash;

}
