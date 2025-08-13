package com.example.Project.Management.bayport.loan.dto.payoff;

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
public class LoanPayOffApiReqDto {

    @XmlElement(name = "Data")
    private LoanPayOffApiReqDataDto loanPayOffApiReqDataDto;

    @XmlElement(name = "Signature")
    private String messageHash;
}
