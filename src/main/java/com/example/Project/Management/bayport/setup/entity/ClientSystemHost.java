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
public class ClientSystemHost implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="client_system_host_id_generator")
	@SequenceGenerator(name="client_system_host_id_generator", sequenceName="seq_client_system_host", initialValue=1, allocationSize = 1)
	@Column(name="client_system_host_id", unique=true, nullable=false, precision=11)
	private Long id;

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
