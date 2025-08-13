package com.dflex.emkopo.app.setup.entity;

import java.io.Serializable;
import java.util.Date;
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
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
/**
 * 
 * @author Augustino Mwageni
 * 
 * Entity Class for Database Table tab_api_version
 *
 */
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name = "tab_api_version")
public class ApiVersion extends CommonEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="api_version_id_generator")
	@SequenceGenerator(name="api_version_id_generator", sequenceName="seq_api_version", initialValue=1, allocationSize = 1)
	@Column(name="id", unique=true, nullable=false, precision=11)
	private Long id;
	
	@GeneratedValue
	@UuidGenerator
	@Column(name="api_version_uid",nullable = false)
	private UUID apiVersionUid;
	
	@PrePersist
    protected void onCreate() {
        setApiVersionUid(java.util.UUID.randomUUID());
    }

	@Column(name="api_version_description", length=1000, nullable=false)
	private String apiVersionDescription;

	@Column(name="release_date", nullable=false)
	private Date releaseDate;

	@Column(name="api_version_number", nullable=false)
	private Long apiVersionNumber;
	
	@Column(name="record_status_id", nullable=false)
	private Long recordStatusId = 1L;
}
