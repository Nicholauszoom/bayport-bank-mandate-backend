package com.example.Project.Management.bayport.crdb.entity;

import com.example.Project.Management.bayport.common.entity.CommonEntity;
import com.example.Project.Management.bayport.crdb.enumerated.MandateRequestType;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "mandate")
@Builder
public class DirectDebitMandate extends CommonEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "[mandate_id_generator")
    @SequenceGenerator(name = "mandate_id_generator", sequenceName = "seq_mandate", initialValue = 1, allocationSize = 1)
    @Column(name = "id", nullable = false, precision = 11)
    private Long id;

    @Column(name = "check_sum", nullable = false)
    private String checksum;

    @Column(name = "partner_id", nullable = false)
    private String partnerId;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "debit_account", nullable = false)
    private String debitAccount;

    @Column(name = "total_amount", nullable = false)
    private BigDecimal totalAmount;

    @Column(name = "installment_amount", nullable = false)
    private BigDecimal installmentAmount;

    @Column(name = "start_date", nullable = false)
    private String startDate; // Format: "DD-MM-YYYY"

    @Column(name = "end_date", nullable = false)
    private String endDate; // Format: "DD-MM-YYYY"

    @Column(name = "frequency_type", nullable = false)
    private String frequencyType; // DAILY, WEEKLY, MONTHLY, YEARLY

    @Column(name = "frequency", nullable = false)
    private Integer frequency; // Conditional, default 1

    @Column(name = "bill_details")
    private String billDetails; // Optional

    @Column(name = "mandate_request_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private MandateRequestType mandateRequestType;
}
