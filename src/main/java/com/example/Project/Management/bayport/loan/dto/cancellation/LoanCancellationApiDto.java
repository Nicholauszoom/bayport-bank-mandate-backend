package com.example.Project.Management.bayport.loan.dto.cancellation;

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
public class LoanCancellationApiDto {

    @XmlElement(name = "Data")
    private LoanCancellationApiReqDataDto loanCancellationApiReqData;

    @XmlElement(name = "Signature")
    private String messageHash;

}
