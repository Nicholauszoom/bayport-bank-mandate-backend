package com.example.Project.Management.bayport.charge.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "client-rv")
public class ClientRv {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "client_rv_id_generator")
    @SequenceGenerator(name = "client_rv_id_generator", sequenceName = "seq_client_rv", initialValue = 1, allocationSize = 1)
    @Column(name = "id", nullable = false, precision = 11)
    private Long id;

    @Column(name = "loan_number", length = 255)
    private String loanNumber;

    @Column(name = "fsp_reference_number", length = 255)
    private String fspReferenceNumber;

    @Column(name = "payment_reference_number", length = 255)
    private String paymentReferenceNumber;

    @Column(name = "check_number", length = 255)
    private String checkNumber;

    @Column(name = "total_pay_off_amount", length = 255)
    private BigDecimal totalPayoffAmount;

    @Column(name = "loan_end_date", length = 255)
    private String loanEndDate;

    @Column(name = "last_deduction_date", length = 255)
    private String lastDeductionDate;

    @Column(name = "total_outstanding_balance", length = 255)
    private BigDecimal totalOutstandingBalance;

    @Column(name = "takeover_amount", length = 255)
    private BigDecimal takeoverAmount;

    @Column(name = "principal_amount", length = 255)
    private BigDecimal principalAmount;

    @Column(name = "installment_amount", length = 255)
    private BigDecimal installmentAmount;


}
