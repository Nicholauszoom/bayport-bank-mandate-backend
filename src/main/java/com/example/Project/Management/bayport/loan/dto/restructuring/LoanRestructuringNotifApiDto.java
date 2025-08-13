package com.example.Project.Management.bayport.loan.dto.restructuring;

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
public class LoanRestructuringNotifApiDto {

    @XmlElement(name = "Data")
    private LoanRestructuringNotifDataApiDto loanRestructuringNotifData;

    @XmlElement(name = "Signature")
    private String messageHash;
}
