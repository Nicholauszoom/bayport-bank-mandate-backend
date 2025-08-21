package com.example.Project.Management.bayport.setup.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name = "tab_permission_group")
public class PermissionGroup implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "permission_group_id_generator")
    @SequenceGenerator(name = "permission_group_id_generator", sequenceName = "seq_permission_group", initialValue=1, allocationSize = 1)
    @Column(name = "id", nullable = false, precision = 11)
    private Long id;

    @Column(name = "permission_group_name",nullable=false)
    private String permissionGroupName;

}
