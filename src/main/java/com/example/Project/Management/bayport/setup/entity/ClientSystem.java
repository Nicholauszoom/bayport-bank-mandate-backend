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
 * Entity Class for Database Table tab_client_system
 *
 */

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name = "tab_client_system")
public class ClientSystem implements Serializable {

	private static final long serialVersionUID = -1373084716740360481L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="client_system_id_generator")
	@SequenceGenerator(name="client_system_id_generator", sequenceName="seq_client_system", initialValue=1, allocationSize = 1)
	@Column(name="id", unique=true, nullable=false, precision=11)
	private Long id;

	@Column(name="client_key", length=50, nullable=false)
	private String clientKey;
	
	@Column(name="client_system_description", length=250, nullable=false)
	private String clientSystemDescription;
	
	@Column(name="record_status_id", nullable=false)
	private Long recordStatusId = 1L;
	
	@ManyToOne(fetch = FetchType.EAGER, optional = true)
	@JoinColumn(name="pki_certificate_id" , nullable=true)
	private PKICertificate pkiCertificate;

}
