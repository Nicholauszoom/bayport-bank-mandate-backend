package com.example.Project.Management.bayport.loan.dto.liquidization;

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
public class LiquidizationNotificationApiReqDto {


    @XmlElement(name = "Data")
    private LoanLiquidizationApiReqDataDto loanLiquidizationApiReqDataDto;

    @XmlElement(name = "Signature")
    private String messageHash;
}