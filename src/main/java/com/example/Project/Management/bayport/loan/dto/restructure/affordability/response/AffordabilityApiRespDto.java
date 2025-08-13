package com.example.Project.Management.bayport.loan.dto.restructure.affordability.response;

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
public class AffordabilityApiRespDto {
    @XmlElement(name = "Data")
    private AffordabilityApirRespDataDto affordabilityApirRespDataDto;

    @XmlElement(name = "Signature")
    private String messageHash;

}
