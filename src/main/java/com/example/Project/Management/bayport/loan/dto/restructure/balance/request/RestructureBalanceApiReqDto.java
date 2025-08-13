package com.example.Project.Management.bayport.loan.dto.restructure.balance.request;


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
public class RestructureBalanceApiReqDto {

    @XmlElement(name = "Data")
    private RestructureBalanceApiReqDataDto loanTopUpBalanceApiReqDataDto;

    @XmlElement(name = "Signature")
    private String messageHash;
}
