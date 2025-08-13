package com.example.Project.Management.bayport.loan.dto.takeover.take_over_balance.response;

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
public class TakeOverBalanceApiRespDto {
    @XmlElement(name = "Data")
    private TakeOverBalanceApiRespDataDto takeOverBalanceApiRespDataDto;

    @XmlElement(name = "Signature")
    private String messageHash;
}
