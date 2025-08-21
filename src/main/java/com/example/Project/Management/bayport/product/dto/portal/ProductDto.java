package com.example.Project.Management.bayport.product.dto.portal;

import com.example.Project.Management.bayport.common.enumerator.Currency;
import com.example.Project.Management.bayport.common.enumerator.RequestAction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDto {

    private Long id;

    private RequestAction action;

    private String deductionCode;

    private String productCode;

    private String productName;

    private String productDescription;

    private Boolean forExecutive;

    private Integer minimumTenure;

    private Integer maximumTenure;

    private Integer interestRate;

    private BigDecimal processFee;

    private BigDecimal insurance;

    private BigDecimal maxLoanAmount;

    private BigDecimal minLoanAmount;

    private BigDecimal otherCharges;

    private String repaymentType;

    private Currency currency;

    private String insuranceType;
}
