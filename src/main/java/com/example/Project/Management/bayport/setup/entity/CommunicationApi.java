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
 * Entity Class for Database Table tab_communication_api
 *
 */

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name = "tab_communication_api", uniqueConstraints= {@UniqueConstraint(columnNames={"api_category_id", "client_system_id"})})
public class CommunicationApi implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="communication_api_id_generator")
	@SequenceGenerator(name="communication_api_id_generator", sequenceName="seq_communication_api", initialValue=1, allocationSize = 1)
	@Column(name="id", unique=true, nullable=false, precision=11)
	private Long id;

	@Column(name="api_description", length=250, nullable=false)
	private String apiDescription;

	@Column(name="apply_digital_signature", nullable=false)
	private Boolean applyDigitalSignature = true;

	@Column(name="api_url", length=250)
	private String apiUrl;
	
	@Column(name = "api_url_status_id")
	private Long apiUrlStatusId;

	@Column(name = "inbound_status_id")
	private Long inboundStatusId;
	
	@Column(name = "outbound_status_id")
	private Long outboundStatusId;
	
	@Column(name = "record_status_id", nullable=false)
	private Long recordStatusId;
	
	@Column(name="api_version_number", nullable=false)
	private Long apiVersionNumber;
	
	@ManyToOne(optional=false, fetch=FetchType.EAGER)
	@JoinColumn(name="api_category_id")
	private ApiCategory apiCategory;
	
	@ManyToOne(optional=false, fetch=FetchType.EAGER)
	@JoinColumn(name="client_system_id", nullable=false)
	private ClientSystem clientSystem;
	
}