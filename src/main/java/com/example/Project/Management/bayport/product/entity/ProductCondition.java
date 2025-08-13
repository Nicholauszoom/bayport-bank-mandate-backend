package com.example.Project.Management.bayport.product.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

/**
 * 
 * @author Augustino Mwageni
 *
 *         Entity class for database table tab_product_condition
 *
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name = "tab_product_condition")
public class ProductCondition implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_condition_id_generator")
	@SequenceGenerator(name = "product_condition_id_generator", sequenceName = "seq_product_condition", initialValue = 1, allocationSize = 1)
	@Column(name = "id", nullable = false, precision = 11)
	private Long id;
	
	@GeneratedValue
	@UuidGenerator
	@Column(name="product_condition_uid",nullable = false)
	private UUID productConditionUid;
	
	@PrePersist
    protected void onCreate() {
        setProductConditionUid(UUID.randomUUID());
    }

	@Column(name = "condition_number", length = 20, nullable = false)
	private String conditionNumber;
	
	@Column(name = "condition_description", length = 255, nullable = false)
	private String conditionDescription;

	@Column(name = "effective_date",nullable = false)
	private Date effectiveDate;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	@JoinColumn(name = "product_id", nullable = true)
	private Product product;
	
	@Column(name="record_status_id", nullable=false)
	private Long recordStatusId = 1L;

}