package com.example.Project.Management.bayport.loan.dto.repayment.response;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@XmlRootElement(name = "MessageDetails")
@XmlAccessorType(XmlAccessType.FIELD)
public class PaymentDetailsRespApiDetailDto {

    @XmlElement(name = "LoanNumber")
    private String loanNumber;

    @XmlElement(name = "FSPCode")
    private String fspCode;

    @XmlElement(name = "FSPReferenceNumber")
    private String fspReferenceNumber;

    @XmlElement(name = "PaymentReferenceNumber")
    private String paymentReferenceNumber;

    @XmlElement(name = "TotalPayoffAmount")
    private BigDecimal totalPayoffAmount;

    @XmlElement(name = "OutstandingBalance")
    private BigDecimal outstandingBalance;

    @XmlElement(name = "FSPBankAccount")
    private String fspBankAccount;

    @XmlElement(name = "FSPBankAccount")
    private String fspbankAccountName;

    @XmlElement(name = "SWIFTCode")
    private String swiftCode;

    @XmlElement(name = "MNOChannels")
    private String mnoChannels;

    @XmlElement(name = "FinalPaymentDate")
    private Date finalPaymentDate;

    @XmlElement(name = "LastDeductionDate")
    private Date lastDeductionDate;

    @XmlElement(name = "LastPayDate")
    private Date lastPayDate;

    @XmlElement(name = "EndDate")
    private Date endDate;

}
