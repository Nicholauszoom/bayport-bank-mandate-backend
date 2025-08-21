package com.example.Project.Management.bayport.loan.dto.repayment;

import com.example.Project.Management.bayport.apis.dto.ApiReqHeaderDto;
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
public class RepaymentNotfiticationApiDataDto {
    @XmlElement(name = "Header")
    private ApiReqHeaderDto requestHeader;

    @XmlElement(name = "MessageDetails")
    private RepaymentNotificationApiDetailDto messageDetails;
}
