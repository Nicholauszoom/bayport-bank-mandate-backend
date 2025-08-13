package com.example.Project.Management.bayport.product.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * 
 * @author Augustino Mwageni
 *
 */
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@XmlRootElement(name = "MessageDetails")
@XmlAccessorType(XmlAccessType.FIELD)
public class ProductRegistrationApiReqDetailDto {

	@XmlElement(name = "DeductionCode")
	private String deductionCode;
	
	@XmlElement(name = "ProductCode")
	private String productCode;

	@XmlElement(name = "ProductName")
	private String productName;

	@XmlElement(name = "ProductDescription")
	private String productDescription;

	@XmlElement(name = "ForExecutive")
	private String forExecutive;

	@XmlElement(name = "MinimumTenure")
	private Integer minimumTenure;

	@XmlElement(name = "MaximumTenure")
	private Integer maximumTenure;

	@XmlElement(name = "InterestRate")
	private Integer interestRate;

	@XmlElement(name = "ProcessFee")
	private BigDecimal processFee;

	@XmlElement(name = "Insurance")
	private BigDecimal insurance;

	@XmlElement(name = "MaxAmount")
	private BigDecimal maxAmount;

	@XmlElement(name = "MinAmount")
	private BigDecimal minAmount;

	@XmlElement(name = "RepaymentType")
	private String erpaymentType;

	@XmlElement(name = "RepaymentType")
	private String repaymentType;

	@XmlElement(name = "Currency")
	private String currency;

	@XmlElement(name = "InsuranceType")
	private String insuranceType;


	@XmlElement(name = "TermsCondition")
	private List<ProductConditionApiReqDetailDto> productConditionList;

}
