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
public class ClientSystem extends CommonEntity implements Serializable {

	private static final long serialVersionUID = -1373084716740360481L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="client_system_id_generator")
	@SequenceGenerator(name="client_system_id_generator", sequenceName="seq_client_system", initialValue=1, allocationSize = 1)
	@Column(name="id", unique=true, nullable=false, precision=11)
	private Long id;
	
	@GeneratedValue
	@UuidGenerator
	@Column(name="client_system_uid",nullable = false)
	private UUID clientSystemUid;
	
	@PrePersist
    protected void onCreate() {
        setClientSystemUid(java.util.UUID.randomUUID());
    }
	
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
