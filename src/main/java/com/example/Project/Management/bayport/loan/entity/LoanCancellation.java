package com.example.Project.Management.bayport.loan.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;
@Getter
@Setter
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

    @Column(name = "reason",  nullable = false)
    private String reason;

    @Column(name = "fspReferenceNumber",  nullable = false)
    private String fspReferenceNumber;

    @OneToOne
    @JoinColumn(name = "loan_id", referencedColumnName = "id")
    private Loan loan;




}
