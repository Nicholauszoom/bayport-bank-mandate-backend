package com.dflex.emkopo.app.setup.entity;

import java.io.Serializable;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import com.dflex.emkopo.commons.utils.CommonEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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
public class CommunicationApi extends CommonEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="communication_api_id_generator")
	@SequenceGenerator(name="communication_api_id_generator", sequenceName="seq_communication_api", initialValue=1, allocationSize = 1)
	@Column(name="id", unique=true, nullable=false, precision=11)
	private Long id;
	
	@GeneratedValue
	@UuidGenerator
	@Column(name="communication_api_uid",nullable = false)
	private UUID communicationApiUid;
	
	@PrePersist
    protected void onCreate() {
        setCommunicationApiUid(java.util.UUID.randomUUID());
    }

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