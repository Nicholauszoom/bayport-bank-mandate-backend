package com.dflex.emkopo.app.setup.entity;

import java.io.Serializable;
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
 * Entity Class for Database Table tab_system_client
 *
 */

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name = "tab_system_client", uniqueConstraints= {@UniqueConstraint(columnNames={"client_id", "client_system_id", "client_category_id"})})
public class SystemClient extends CommonEntity implements Serializable {

	private static final long serialVersionUID = 6984936841911656937L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="system_client_id_generator")
	@SequenceGenerator(name="system_client_id_generator", sequenceName="seq_system_client", initialValue=1, allocationSize = 1)
	@Column(name="id", unique=true, nullable=false, precision=11)
	private Long id;
	
	@GeneratedValue
	@UuidGenerator
	@Column(name="system_client_uid",nullable = false)
	private UUID systemClientUid;
	
	@PrePersist
    protected void onCreate() {
        setSystemClientUid(java.util.UUID.randomUUID());
    }
	
	@Column(name="client_id", nullable=false)
	private Long clientId;
	
	@Column(name="client_code", nullable=false)
	private String clientCode;
	
	@Column(name="client_system_id", nullable=false)
	private Long clientSystemId;
	
	@Column(name="client_category_id", nullable=false)
	private Long clientCategoryId;
	
	@Column(name="record_status_id", nullable=false)
	private Long recordStatusId = 1L;
	
}
