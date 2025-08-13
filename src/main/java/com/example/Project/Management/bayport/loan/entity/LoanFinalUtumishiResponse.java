package com.example.Project.Management.bayport.loan.entity;

import jakarta.persistence.*;
import lombok.*;



@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name = "tab_loan_final_utumishi_response")
public class LoanFinalUtumishiResponse {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "loan_final_utumishi_response_id_generator")
    @SequenceGenerator(name = "loan_final_utumishi_response_id_generator", sequenceName = "seq_loan_final_utumishi_response", initialValue = 1, allocationSize = 1)
    @Column(name = "id", nullable = false, precision = 11)
    private Long id;

    @Column(name = "approval", length = 12,nullable=true)
    private String approval;

    @Column(name = "reason", length = 255,nullable=true)
    private String reason;

    @Column(name = "application_number", length = 50,nullable=false)
    private String applicationNumber;

    @Column(name = "loan_number", length = 50,nullable=true)
    private String loanNumber;

    @Column(name = "reference_number", length = 255,nullable=true)
    private String referenceNumber;

    @OneToOne
    private Loan loan;
}
