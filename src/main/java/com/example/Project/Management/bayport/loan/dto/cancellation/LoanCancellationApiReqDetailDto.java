package com.example.Project.Management.bayport.loan.dto.cancellation;

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
@XmlRootElement(name = "MessageDetails")
@XmlAccessorType(XmlAccessType.FIELD)
public class LoanCancellationApiReqDetailDto {

    @XmlElement(name = "ApplicationNumber")
    private String applicationNumber;

    @XmlElement(name = "Reason")
    private String reason;

    @XmlElement(name = "FSPReferenceNumber")
    private String fspReferenceNumber;

    @XmlElement(name = "LoanNumber")
    private String loanNumber;

}
