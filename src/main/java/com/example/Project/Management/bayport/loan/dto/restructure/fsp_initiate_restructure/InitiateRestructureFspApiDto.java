package com.example.Project.Management.bayport.loan.dto.restructure.fsp_initiate_restructure;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@JsonIgnoreProperties(ignoreUnknown = true)
public class InitiateRestructureFspApiDto {
    @XmlElement(name = "Data")
    private InitiateRestructureFspApiDataDto initiateRestructureFspApiDataDto;

    @XmlElement(name = "Signature")
    private String messageHash;
}
