package com.example.Project.Management.bayport.loan.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name = "tab_loan_cancellation")
public class LoanCancellation {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "loan_cancellation_id_generator")
    @SequenceGenerator(name = "loan_cancellation_id_generator", sequenceName = "seq_loan_cancellation", initialValue = 1, allocationSize = 1)
    @Column(name = "id", nullable = false, precision = 11)
    private Long id;

    @GeneratedValue
    @UuidGenerator
    @Column(name="loan_cancellation_uid",nullable = false)
    private UUID loanCancellationUid;

    @PrePersist
    protected void onCreate() {
        setLoanCancellationUid(UUID.randomUUID());
    }

    @Column(name = "reason",  nullable = false)
    private String reason;

    @Column(name = "fspReferenceNumber",  nullable = false)
    private String fspReferenceNumber;

    @OneToOne
    @JoinColumn(name = "loan_id", referencedColumnName = "id")
    private Loan loan;




}
