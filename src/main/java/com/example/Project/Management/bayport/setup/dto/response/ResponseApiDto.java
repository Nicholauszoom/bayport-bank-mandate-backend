package com.dflex.emkopo.app.setup.dto.response;

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
public class ResponseApiDto {

    @XmlElement(name = "Data")
    private ResponseDataApiDto responseDataDto;

    @XmlElement(name = "Signature")
    private String messageHash;

}
