package com.example.Project.Management.bayport.fsp.models.entities;

import com.example.Project.Management.bayport.common.entity.CommonEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.io.Serializable;
import java.util.UUID;

/**
 * 
 * @author Augustino Mwageni
 *
 *         Entity class for database table tab_fsp_category
 *
 */

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name = "tab_fsp_category")
public class FinancialServiceProviderCategory implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fsp_category_id_generator")
	@SequenceGenerator(name = "fsp_category_id_generator", sequenceName = "seq_fsp_category", initialValue = 1, allocationSize = 1)
	@Column(name = "id", nullable = false, precision = 11)
	private Long id;
	
	@Column(name="category_code", nullable=false, unique = true, length = 20)
	private String categoryCode;

	@Column(name="category_name",nullable=false,length = 50)
	private String categoryName;
	
	@Column(name="record_status_id", nullable=false)
	private Long recordStatusId = 1L;
}