package com.example.Project.Management.bayport.setup.entity;

import com.example.Project.Management.bayport.common.entity.CommonEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.io.Serializable;
import java.util.UUID;

/**
 * @author Augustino Mwageni
 * 
 * Entity Class for Database Table tab_api_category
 *
 */

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name = "tab_api_category")
public class ApiCategory implements Serializable {

	private static final long serialVersionUID = -80992527267230465L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="api_category_id_generator")
	@SequenceGenerator(name="api_category_id_generator", sequenceName="seq_api_category", initialValue=1, allocationSize = 1)
	@Column(name="id", unique=true, nullable=false, precision=11)
	private Long id;

	@Column(name="api_category_description", nullable=false, length=250)
	private String apiCategoryDescription;
	
	@Column(name="record_status_id", nullable=false)
	private Long recordStatusId = 1L;
}