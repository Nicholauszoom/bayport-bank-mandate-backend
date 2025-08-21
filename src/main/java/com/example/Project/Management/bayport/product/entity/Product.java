package com.example.Project.Management.bayport.product.entity;

import com.example.Project.Management.bayport.common.entity.CommonEntity;
import com.example.Project.Management.bayport.common.entity.Status;
import com.example.Project.Management.bayport.common.enumerator.Currency;
import com.example.Project.Management.bayport.fsp.models.entities.FinancialServiceProvider;
import com.example.Project.Management.bayport.setup.entity.UtumishiResponse;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name = "tab_product")
public class Product extends CommonEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_id_generator")
	@SequenceGenerator(name = "product_id_generator", sequenceName = "seq_product", initialValue = 1, allocationSize = 1)
	@Column(name = "id", nullable = false, precision = 11)
	private Long id;

	@Column(name = "fsp_code", length = 10, nullable = false)
	private String fspCode;

	@Column(name = "fsp_name", length = 100, nullable = false)
	private String fspName;

	@Column(name = "product_code", length = 10, nullable = false)
	private String productCode;

	@Column(name = "deduction_code", length = 10, nullable = false)
	private String deductionCode;

	@Column(name = "product_name", length = 255, nullable = false)
	private String productName;

	@Column(name = "product_description", length = 255, nullable = false)
	private String productDescription;

	@Column(name = "minimum_tenure", nullable = false)
	private Integer minimumTenure;

	@Column(name = "maximum_tenure", nullable = false)
	private Integer maximumTenure;

	@Column(name = "interest_rate", nullable = false)
	private int interestRate;

	@Column(name = "processing_fee", nullable = false)
	private BigDecimal processingFee = new BigDecimal("0.00");

	@Column(name = "insurance")
	private BigDecimal insurance = new BigDecimal("0.00");

	@Column(name = "min_loan_amount", nullable = false)
	private BigDecimal minLoanAmount = new BigDecimal("0.00");

	@Column(name = "max_loan_amount", nullable = false)
	private BigDecimal maxLoanAmount = new BigDecimal("0.00");

	@Column(name = "other_charges", nullable = false)
	private BigDecimal otherCharges = new BigDecimal("0.00");

	@Column(name = "repayment_type", length = 10, nullable = false)
	private String repaymentType;

	@Column(name = "is_executive", nullable = false)
	private Boolean isExecutive = false;

	@Column(name = "record_status_id", nullable = false)
	private Long recordStatusId = 1L;

	@Column(name = "registration_date")
	private Date registrationDate;

	@Column(name = "deregistration_date")
	private Date deregistrationDate;

	@Column(name = "registration_status")
	private String registrationStatus;

	@Column(name = "currency_name")
	private String currencyName;

	@Column(name = "annual_percentage_rate")
	private BigDecimal annualPercentageRate;

	@Column(name = "process_fee")
	private BigDecimal processFee;

	@Column(name = "insurance_type")
	private String insuranceType;

	@Enumerated(EnumType.STRING)
	@Column(name = "status")
	private Status status;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "fsp_id", nullable = false)
	private FinancialServiceProvider financialServiceProvider;

	@Enumerated(EnumType.STRING)
	@Column(name = "currency")
	private Currency currency;

	@OneToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "response_id")
	private UtumishiResponse utumishiResponseEntity;

}