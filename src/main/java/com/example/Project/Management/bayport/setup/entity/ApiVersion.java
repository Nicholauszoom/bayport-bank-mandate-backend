package com.example.Project.Management.bayport.setup.entity;

import com.example.Project.Management.bayport.common.entity.CommonEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;
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
public class ApiVersion implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="api_version_id_generator")
	@SequenceGenerator(name="api_version_id_generator", sequenceName="seq_api_version", initialValue=1, allocationSize = 1)
	@Column(name="id", unique=true, nullable=false, precision=11)
	private Long id;

	@Column(name="api_version_description", length=1000, nullable=false)
	private String apiVersionDescription;

	@Column(name="release_date", nullable=false)
	private Date releaseDate;

	@Column(name="api_version_number", nullable=false)
	private Long apiVersionNumber;
	
	@Column(name="record_status_id", nullable=false)
	private Long recordStatusId = 1L;
}
