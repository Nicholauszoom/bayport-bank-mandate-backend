package com.example.Project.Management.bayport.loan.dto.topup.balance.response;

import com.dflex.emkopo.api.dto.ApiReqHeaderDto;
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
@XmlRootElement(name = "Data")
@XmlAccessorType(XmlAccessType.FIELD)
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoanTopUpBalanceApiRespDataDto {

    @XmlElement(name = "Header")
    private ApiReqHeaderDto requestHeader;

    @XmlElement(name = "MessageDetails")
    private LoanTopUpPayOffBalanceApiRespDetailDto loanTopUpBalanceApiRespDetailDto;
}
