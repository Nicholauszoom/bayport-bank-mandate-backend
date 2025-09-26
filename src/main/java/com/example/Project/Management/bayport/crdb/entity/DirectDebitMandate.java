package com.example.Project.Management.bayport.crdb.entity;

import com.example.Project.Management.bayport.common.entity.CommonEntity;
import com.example.Project.Management.bayport.crdb.enumerated.MandateRequestType;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.Date;

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

    @Column(name = "request_id")
    private String requestId;

    @Column(name = "phone_number", nullable = false, length = 12)
    private String phoneNumber;

    @Column(name = "debit_account", nullable = false, length = 13)
    private String debitAccount;

    @Column(name = "total_amount", nullable = false)
    private BigDecimal totalAmount;

    @Column(name = "installment_amount", nullable = false)
    private BigDecimal installmentAmount;

    @Column(name = "start_date", nullable = false)
    private Date startDate; // Format: "DD-MM-YYYY"

    @Column(name = "end_date", nullable = false)
    private Date endDate; // Format: "DD-MM-YYYY"

    @Column(name = "frequency_type", nullable = false)
    private String frequencyType; // DAILY, WEEKLY, MONTHLY, YEARLY

    @Column(name = "frequency", nullable = false)
    private Integer frequency; // Conditional, default 1

    @Column(name = "bill_details")
    private String billDetails;

    @Column(name = "failed_reason")
    private String responseDescription;

    @Column(name = "response_code")
    private String statusCode;

    @Column(name = "error_code")
    private String errorCode;

    @Column(name = "error_message")
    private String errorMessage;

    @Column(name = "mandate_request_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private MandateRequestType mandateRequestType;

    @Column(name = "submitted_date")
    private String submittedDate; // Format: "DD-MM-YYYY"

}