package com.example.Project.Management.bayport.crdb.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class DirectDebitMandateRequest {
    @JsonProperty("Checksum")
    private String checksum;

    @JsonProperty("PartnerId")
    private String partnerId;

    @JsonProperty("PhoneNumber")
    private String phoneNumber;

    @JsonProperty("DebitAccount")
    private String debitAccount;

    @JsonProperty("TotalAmount")
    private BigDecimal totalAmount;

    @JsonProperty("InstallmentAmount")
    private BigDecimal installmentAmount;

    @JsonProperty("StartDate")
    private String startDate;

    @JsonProperty("EndDate")
    private String endDate;

    @JsonProperty("FrequencyType")
    private String frequencyType;

    @JsonProperty("Frequency")
    private Integer frequency;

    @JsonProperty("BillDetails")
    private String billDetails;
}