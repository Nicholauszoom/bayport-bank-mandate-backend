package com.example.Project.Management.bayport.status.entity;

import com.example.Project.Management.bayport.loan.entity.Loan;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name = "tab_status")
public class Status {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "status_id_generator")
    @SequenceGenerator(name = "status_id_generator", sequenceName = "seq_status", initialValue = 1, allocationSize = 1)
    @Column(name = "id", nullable = false, precision = 11)
    private Long id;

    @Column(name = "status_name", length = 10, nullable = true)
    private String name;

    @Column(name = "status_description", length = 10, nullable = true)
    private String description;
}
