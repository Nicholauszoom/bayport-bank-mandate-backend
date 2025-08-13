package com.example.Project.Management.bayport.loan.dto.ess_notification_response;

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
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "request")
public class EssRequestLoanInitialApprovePortalDto {

    @XmlElement(name = "ApplicationNumber")
    private String applicationNumber;
    @XmlElement(name = "Reason")
    private String reason;
    @XmlElement(name = "FSPReferenceNumber")
    private String fspReferenceNumber;
    @XmlElement(name = "LoanNumber")
    private String loanNumber;
    @XmlElement(name = "TotalAmountToPay")
    private String totalAmountToPay;
    @XmlElement(name = "OtherCharges")
    private String otherCharges;
    @XmlElement(name = "Approval")
    private String approval;

}
