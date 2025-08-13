package com.dflex.emkopo.app.fsp.models.entities;

import java.io.Serializable;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import com.dflex.emkopo.commons.utils.CommonEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 
 * @author Augustino Mwageni
 *
 *         Entity class for database table tab_fsp_category
 *
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name = "tab_fsp_category")
public class FinancialServiceProviderCategory extends CommonEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fsp_category_id_generator")
	@SequenceGenerator(name = "fsp_category_id_generator", sequenceName = "seq_fsp_category", initialValue = 1, allocationSize = 1)
	@Column(name = "id", nullable = false, precision = 11)
	private Long id;
	
	@GeneratedValue
	@UuidGenerator
	@Column(name = "fsp_category_uid", nullable = false)
	private UUID fspUid;

	@PrePersist
	protected void onCreate() {
		setFspUid(java.util.UUID.randomUUID());
	}
	
	@Column(name="category_code", nullable=false, unique = true, length = 20)
	private String categoryCode;

	@Column(name="category_name",nullable=false,length = 50)
	private String categoryName;
	
	@Column(name="record_status_id", nullable=false)
	private Long recordStatusId = 1L;
}