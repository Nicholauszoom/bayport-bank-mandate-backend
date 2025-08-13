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
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Augustino Mwageni
 * 
 * The persistent class for the tab_client_system_host database table.
 * 
 */
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name="tab_client_system_host")
public class ClientSystemHost extends CommonEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="client_system_host_id_generator")
	@SequenceGenerator(name="client_system_host_id_generator", sequenceName="seq_client_system_host", initialValue=1, allocationSize = 1)
	@Column(name="client_system_host_id", unique=true, nullable=false, precision=11)
	private Long id;
	
	@GeneratedValue
	@UuidGenerator
	@Column(name="client_system_host_uid",nullable = false)
	private UUID clientSystemHostUid;
	
	@PrePersist
    protected void onCreate() {
        setClientSystemHostUid(java.util.UUID.randomUUID());
    }
	
	@Column(name="host_name", length=255, nullable=false)
	private String hostName;
	
	@Column(name="ip_address", length=50, nullable=false)
	private String ipAddress;
	
	@Column(name="ip_version",nullable=false)
	private Long ipVersion;
	
	@Column(name = "record_status_id", nullable=false)
	private Long recordStatusId;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name="client_system_id", nullable=false)
	private ClientSystem clientSystem;

}
