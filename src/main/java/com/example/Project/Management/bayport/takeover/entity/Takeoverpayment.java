package com.example.Project.Management.bayport.takeover.entity;

import com.example.Project.Management.bayport.common.entity.CommonEntity;
import com.example.Project.Management.bayport.common.enumerator.PaymentStatus;
import com.example.Project.Management.bayport.common.enumerator.RequestType;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlElement;
import lombok.*;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name = "tekeover_payment")
@Builder
public class Takeoverpayment extends CommonEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "takeover_payment_id_generator")
    @SequenceGenerator(name = "takeover_payment_id_generator", sequenceName = "seq_takeover_payment", initialValue = 1, allocationSize = 1)
    @Column(name = "id", nullable = false, precision = 11)
    private Long id;
    @Column(name = "application_number", length = 100)
    private String applicationNumber;

    @Column(name = "loan_number", length = 100)
    private String loanNumber;

    @Column(name = "balance_id_result_payment")
    private Long takeoverBalanceId;

    @Column(name = "reason", length = 255)
    private String reason;

    @Column(name = "fsp_reference_number", length = 255)
    private String fspReferenceNumber;

    @Column(name = "payment_reference_number", length = 255)
    private String paymentReferenceNumber;

    @Column(name = "total_payoff_amount")
    private BigDecimal totalPayoffAmount;

    @Column(name = "payment_date")
    private String paymentDate;

    @Column(name= "payment_advice",columnDefinition = "TEXT")
    private String paymentAdvice;

    @Column(name= "payment_advice_attachment",columnDefinition = "TEXT")
    private String paymentAdviceAttachment;

    @Column(name = "request_type")
    @Enumerated(EnumType.STRING)
    private RequestType requestType;

    @Column(name = "acknoledge_remarks")
    private String acknoledgeRemarks;

    @Column(name = "payment_status")
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

}
