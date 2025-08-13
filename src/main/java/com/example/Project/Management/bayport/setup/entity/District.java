package com.dflex.emkopo.app.setup.entity;

import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name = "tab_district")
public class District {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "district_id_generator")
    @SequenceGenerator(name = "district_id_generator", sequenceName = "seq_district", initialValue = 1, allocationSize = 1)
    @Column(name = "id", nullable = false, precision = 11)
    private Long id;


    @Column(name = "district_code", length = 50, nullable = false)
    private String districtCode;


    @Column(name = "district_name", length = 100, nullable = false)
    private String districtName;


    @Column(name="record_status_id", nullable=false)
    private Long recordStatusId = 1L;


}
