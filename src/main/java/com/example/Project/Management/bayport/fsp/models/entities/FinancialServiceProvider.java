package com.dflex.emkopo.app.fsp.models.entities;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import com.dflex.emkopo.app.product.entity.Product;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.dflex.emkopo.app.users.models.User;
import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;

import com.dflex.emkopo.commons.utils.CommonEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 
 * @author Augustino Mwageni
 *
 *         Entity class for database table tab_fsp
 *
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name = "tab_fsp")
public class FinancialServiceProvider  extends CommonEntity implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fsp_id_generator")
	@SequenceGenerator(name = "fsp_id_generator", sequenceName = "seq_fsp", allocationSize = 1)
	@Column(name = "id", nullable = false, precision = 11)
	private Long id;
	
	@GeneratedValue
	@UuidGenerator
	@Column(name = "fsp_uid", nullable = false)
	private UUID fspUid;

	@PrePersist
	protected void onCreate() {
		setFspUid(java.util.UUID.randomUUID());
	}

	@Column(name = "fsp_code", length = 10, nullable = false)
	private String fspCode;

	@Column(name = "fsp_name", length = 100, nullable = false)
	private String fspName;

	@Column(name = "fsp_short_name", length = 50, nullable = false)
	private String fspShortName;

	@Column(name = "fsp_reference_number", length = 50, nullable = false)
	private String fspReferenceNumber;
	
	@Column(name="record_status_id", nullable=false)
	private Long recordStatusId = 1L;

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "fsp_category_id", nullable = false)
	private FinancialServiceProviderCategory financialServiceProviderCategory;

//	@OneToMany(mappedBy = "financialServiceProvider")
////	@JsonIgnore
//	private List<Product> products;

	@OneToMany(mappedBy = "financialServiceProvider")
	private List<FinancialServiceProviderBranch> branches;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	private List<User> users;
}