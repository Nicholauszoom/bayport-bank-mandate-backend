package com.example.Project.Management.bayport.crdb.dto.portal;

import com.example.Project.Management.bayport.crdb.enumerated.MandateRequestType;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
public class DirectDebitMandateDto {
    private Long id;

    private String checksum;

    private String partnerId;

    private String phoneNumber;

    private String debitAccount;

    private BigDecimal totalAmount;

    private BigDecimal installmentAmount;

    private String startDate;

    private String endDate;

    private String frequencyType;

    private Integer frequency;

    private String billDetails;

    private MandateRequestType mandateRequestType;

    private Date createdDate;

    // response
    private String statusCode;
    private String responseDescription;
    private String errorCode;
    private String errorMessage;
    private String submittedDate;

}