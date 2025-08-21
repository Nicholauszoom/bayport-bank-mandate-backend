package com.example.Project.Management.bayport.fsp.models.entities;

import com.example.Project.Management.bayport.auth.user.entity.User;
import com.example.Project.Management.bayport.common.entity.CommonEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

/**
 * 
 * @author Augustino Mwageni
 *
 *         Entity class for database table tab_fsp
 *
 */
@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name = "tab_fsp")
public class FinancialServiceProvider implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fsp_id_generator")
	@SequenceGenerator(name = "fsp_id_generator", sequenceName = "seq_fsp", allocationSize = 1)
	@Column(name = "id", nullable = false, precision = 11)
	private Long id;

	@Column(name = "fsp_code", length = 10, nullable = false)
	private String fspCode;

	@Column(name = "fsp_name", length = 100, nullable = false)
	private String fspName;

	@Column(name = "fsp_short_name", length = 50, nullable = false)
	private String fspShortName;

	@Column(name = "fsp_reference_number", length = 50, nullable = false)
	private String fspReferenceNumber;
	
	@Column(name="record_status_id", nullable=false)
	private Long recordStatusId = 1L;

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "fsp_category_id", nullable = false)
	private FinancialServiceProviderCategory financialServiceProviderCategory;

	@OneToMany(mappedBy = "financialServiceProvider")
	private List<FinancialServiceProviderBranch> branches;
}