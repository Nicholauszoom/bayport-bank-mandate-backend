package com.example.Project.Management.bayport.disbursement.dto.portal;

import com.example.Project.Management.bayport.common.enumerator.RequestAction;
import jakarta.persistence.Column;
import jakarta.xml.bind.annotation.XmlElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DisbursementDto {

    private RequestAction action;

    private String applicationNumber;

    private String reason;

    private String fspReferenceNumber;

    private String loanNumber;

    private BigDecimal totalAmountToPay;

    private String disbursementDate;

    @Column(columnDefinition = "TEXT")
    private String paymentAdvice;

    private String paymentAdviceAttachment;
}
