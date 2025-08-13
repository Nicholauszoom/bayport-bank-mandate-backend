package com.dflex.emkopo.app.disbursement.entity;

import com.dflex.emkopo.app.loan.entity.Loan;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name = "tab_disbursement")
public class Disbursement implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "disbursement_id_generator")
    @SequenceGenerator(name = "disbursement_id_generator", sequenceName = "seq_disbursement", initialValue = 1, allocationSize = 1)
    @Column(name = "id", nullable = false, precision = 11)
    private Long id;

    @GeneratedValue
    @UuidGenerator
    @Column(name="disbursement_uid",nullable = false)
    private UUID disbursementUid;

    @Column(name = "disbursement_date")
    private Date disbursementDate;

    @Column(name = "disbursement_amount")
    private BigDecimal disbursementAmount;

    @Column(name = "disbursement_status")
    private String disbursementStatus;

    @Column(name = "disbursement_method")
    private String disbursementMethod;

    @Column(name = "disbursement_purpose")
    private String disbursementPurpose;

    @Column(name = "disbursement_reason")
    private String reason;

    @Column(name = "recipient_account")
    private String recipientAccount;

    @Column(name = "applivation_number")
    private String applicationNumber;

    @Column(name = "loan_number")
    private String loanNumber;

    //connect loan entity with disbursement entity
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "loan_id", nullable = false)
    private Loan loan;
}
