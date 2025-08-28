package com.example.Project.Management.bayport.balance.dto.portal;


import com.example.Project.Management.bayport.common.enumerator.RequestType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BalanceDto {

    private Long id;

    private String checkNumber;

    private String loanNumber;

    private String firstName;

    private String middleName;

    private String lastName;

    private String voteCode;

    private String voteName;

    private BigDecimal deductionAmount;

    private String deductionCode;

    private String deductionName;

    private BigDecimal deductionBalance;

    private String paymentOption;

    // response
    private String fSPReferenceNumber;

    private String paymentReferenceNumber;

    private BigDecimal totalPayoffAmount;

    private BigDecimal outstandingBalance;

    private String finalPaymentDate;

    private String lastDeductionDate;

    private String lastPayDate;

    private String endDate;

    private RequestType requestType;

    private Date createdDate;
}
