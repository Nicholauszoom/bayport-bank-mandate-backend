package com.dflex.emkopo.app.setup.entity;

import java.io.Serializable;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import com.dflex.emkopo.commons.utils.CommonEntity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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
public class ApiCategory extends CommonEntity implements Serializable {

	private static final long serialVersionUID = -80992527267230465L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="api_category_id_generator")
	@SequenceGenerator(name="api_category_id_generator", sequenceName="seq_api_category", initialValue=1, allocationSize = 1)
	@Column(name="id", unique=true, nullable=false, precision=11)
	private Long id;
	
	@GeneratedValue
	@UuidGenerator
	@Column(name="api_category_uid",nullable = false)
	private UUID apiCategoryUid;
	
	@PrePersist
    protected void onCreate() {
        setApiCategoryUid(java.util.UUID.randomUUID());
    }

	@Column(name="api_category_description", nullable=false, length=250)
	private String apiCategoryDescription;
	
	@Column(name="record_status_id", nullable=false)
	private Long recordStatusId = 1L;
}