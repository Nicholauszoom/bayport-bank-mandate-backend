package com.dflex.emkopo.app.status.entity;

import com.dflex.emkopo.app.loan.entity.Loan;
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

    @GeneratedValue
    @UuidGenerator
    @Column(name="status_uid",nullable = false)
    private UUID statusUid;

    @PrePersist
    protected void onCreate() {
        setStatusUid(java.util.UUID.randomUUID());
    }

    @Column(name = "status_name", length = 10, nullable = true)
    private String name;

    @Column(name = "status_description", length = 10, nullable = true)
    private String description;

    @OneToMany(mappedBy = "status")
    private List<Loan> loans;
}
