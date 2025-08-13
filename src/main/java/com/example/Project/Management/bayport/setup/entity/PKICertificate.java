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
 * @author Augustino Mwageni
 * 
 * Entity Class for Database Table tab_pki_certificate
 *
 */

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name = "tab_pki_certificate")
public class PKICertificate extends CommonEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="pki_certificate_id_generator")
	@SequenceGenerator(name="pki_certificate_id_generator", sequenceName="seq_pki_certificate", initialValue=1, allocationSize = 1)
	@Column(name="id", unique=true, nullable=false, precision=11)
	private Long id;
	
	@GeneratedValue
	@UuidGenerator
	@Column(name="pki_certificate_uid",nullable = false)
	private UUID pkiCertificateUid;
	
	@PrePersist
    protected void onCreate() {
        setPkiCertificateUid(java.util.UUID.randomUUID());
    }

	@Column(name="certificate_alias", length=100, nullable=false)
	private String certificateAlias;
	
	@Column(name="certificate_passphrase", length=100, nullable=false)
	private String certificatePassphrase;
	
	@Column(name="certificate_issued_date", nullable=false)
	private Date certificateIssuedDate;
	
	@Column(name="certificate_expiry_date", nullable=false)
	private Date certificateExpiryDate;
	
	@Column(name="certificate_filename", nullable=false,length=255)
	private String certificateFilename;
	
	@Column(name="certificate_serial_number", nullable=false,length=255)
	private String certificateSerialNumber;
	
	@Column(name = "record_status_id", nullable=false)
	private Long recordStatusId;
	
	@Column(name="certificate_category", nullable=false,length=50)
	private String certificateCategory;
	
	@Column(name="certificate_algorithm",length=255, nullable=false)
	private String certificateAlgorithm;
	 
	@Column(name="internal_pki_certificate_id")
	private Long internalPkiCertificateId;
}