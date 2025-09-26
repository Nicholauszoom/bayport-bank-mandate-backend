package com.example.Project.Management.bayport.crdb.dto.portal;

import com.example.Project.Management.bayport.crdb.enumerated.MandateRequestType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MandateReportDto {
    private Long id;
    private String partnerId;
    private String phoneNumber;
    private String debitAccount;
    private BigDecimal totalAmount;
    private BigDecimal installmentAmount;
    private Date startDate;
    private Date endDate;
    private String frequencyType;
    private Integer frequency;
    private String billDetails;
    private MandateRequestType mandateRequestType;
    private String statusCode;
    private String responseDescription;
    private String errorCode;
    private String errorMessage;
    private String submittedDate;
    private Date createdDate;
    private String status;
    private String failureReason;
}