package com.example.Project.Management.bayport.product.entity;

import com.dflex.emkopo.app.fsp.models.entities.FinancialServiceProvider;
import com.dflex.emkopo.app.setup.entity.Currency;
import com.dflex.emkopo.app.setup.entity.UtumishiResponse;
import com.dflex.emkopo.app.status.entity.Status;
import com.dflex.emkopo.commons.utils.CommonEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

/**
 * 
 * @author Augustino Mwageni
 *
 *         Entity class for database table tab_product 
 */

@Data
@AllArgsConstructor
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

	@GeneratedValue
	@UuidGenerator
	@Column(name="product_uid",nullable = false)
	private UUID productUid;

	@PrePersist
	protected void onCreate() {
		setProductUid(UUID.randomUUID());
	}

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
	private BigDecimal processingFee = new BigDecimal(0.00);

	@Column(name = "insurance")
	private BigDecimal insurance = new BigDecimal(0.00);

	@Column(name = "min_loan_amount", nullable = false)
	private BigDecimal minLoanAmount = new BigDecimal(0.00);

	@Column(name = "max_loan_amount", nullable = false)
	private BigDecimal maxLoanAmount = new BigDecimal(0.00);

	@Column(name = "other_charges", nullable = false)
	private BigDecimal otherCharges = new BigDecimal(0.00);

	@Column(name = "repayment_type", length = 10, nullable = false)
	private String repaymentType;

	@Column(name = "is_executive", nullable = false)
	private Boolean isExecutive = false;

	@Column(name="record_status_id", nullable=false)
	private Long recordStatusId = 1L;

	@Column(name = "registration_date")
	private Date registrationDate;

	@Column(name="deregistration_date")
	private Date deregistrationDate;

	@Column(name="registration_status")
	private String registrationStatus;

	@Column(name="currency_name")
	private String currencyName;

	@Column(name="annual_percentage_rate")
	private BigDecimal annualPercentageRate;

	@ManyToOne
	@JoinColumn(name = "status_id", nullable = true)
	private Status status;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "fsp_id", nullable = false)
	private FinancialServiceProvider financialServiceProvider;


	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "ccy_id", nullable = false)
	@JsonIgnore
	private Currency currency;



	@OneToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "response_id")
	private UtumishiResponse utumishiResponseEntity;

	@Override
	public String toString() {
		return "Product{" +
				"id=" + id +
				", productUid=" + productUid +
				", fspCode='" + fspCode + '\'' +
				", fspName='" + fspName + '\'' +
				", productCode='" + productCode + '\'' +
				", productName='" + productName + '\'' +
				", productDescription='" + productDescription + '\'' +
				", minimumTenure=" + minimumTenure +
				", maximumTenure=" + maximumTenure +
				", interestRate=" + interestRate +
				", processingFee=" + processingFee +
				", insurance=" + insurance +
				", minLoanAmount=" + minLoanAmount +
				", maxLoanAmount=" + maxLoanAmount +
				", otherCharges=" + otherCharges +
				", repaymentType='" + repaymentType + '\'' +
				", isExecutive=" + isExecutive +
				", recordStatusId=" + recordStatusId +
				", registrationDate=" + registrationDate +
				", deregistrationDate=" + deregistrationDate +
				", registrationStatus='" + registrationStatus + '\'' +
				", status=" + (status != null ? status.getId() : null) +
				", financialServiceProvider=" + (financialServiceProvider != null ? financialServiceProvider.getId() : null) +
				", currency=" + (currency != null ? currency.getId() : null) +
				", utumishiResponseEntity=" + (utumishiResponseEntity != null ? utumishiResponseEntity.getId() : null) +
				'}';
	}
}