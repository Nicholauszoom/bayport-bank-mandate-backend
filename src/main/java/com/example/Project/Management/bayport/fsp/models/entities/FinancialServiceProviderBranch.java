package com.dflex.emkopo.app.fsp.models.entities;

import com.dflex.emkopo.app.setup.entity.District;
import com.dflex.emkopo.commons.utils.CommonEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.io.Serializable;



@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name = "tab_fsp_branches")
public class FinancialServiceProviderBranch extends CommonEntity implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fsp_branches_id_generator")
    @SequenceGenerator(name = "fsp_branches_id_generator", sequenceName = "seq_branches", initialValue = 1, allocationSize = 1)
    @Column(name = "id", nullable = false, precision = 11)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "fsp_id", nullable = false)
    private FinancialServiceProvider financialServiceProvider;


    @Column(name = "branch_code", length = 50, nullable = false)
    private String branchCode;


    @Column(name = "branch_name", length = 100, nullable = false)
    private String branchName;


    @Column(name = "location", length = 100, nullable = false)
    private String location;


    @Column(name="record_status_id", nullable=false)
    private Long recordStatusId = 1L;


    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "district_id", nullable = false)
    private District district;


}
