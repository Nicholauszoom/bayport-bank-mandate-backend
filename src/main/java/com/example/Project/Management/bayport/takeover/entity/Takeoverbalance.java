package com.example.Project.Management.bayport.takeover.entity;

import com.example.Project.Management.bayport.common.entity.CommonEntity;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlElement;
import lombok.*;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name = "tekeover_balance")
@Builder
public class Takeoverbalance extends CommonEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "takeover_balance_id_generator")
    @SequenceGenerator(name = "takeover_balance_id_generator", sequenceName = "seq_takeover_balance", initialValue = 1, allocationSize = 1)
    @Column(name = "id", nullable = false, precision = 11)
    private Long id;

    @Column(name = "check_number", length = 100)
    private String checkNumber;

    @Column(name = "loan_number", length = 100)
    private String loanNumber;

    @Column(name = "fsp_code", length = 100)
    private String fspCode;

    @Column(name = "fsp_reference_number", length = 100)
    private String fSPReferenceNumber;

    @Column(name = "payment_reference_number", length = 100)
    private String paymentReferenceNumber;

    @Column(name = "total_payoff_amount", length = 100)
    private BigDecimal totalPayoffAmount;

    @Column(name = "outstanding_balance", length = 100)
    private BigDecimal outstandingBalance;

    @Column(name = "fsp_bank_account", length = 100)
    private String fspBankAccount;

    @Column(name = "fsp_bank_account_name", length = 100)
    private String fspBankAccountName;

    @Column(name = "swift_code", length = 100)
    private String swiftCode;

    @Column(name = "mno_channels", length = 100)
    private String mnoChannels;

    @Column(name = "final_payment_date", length = 100)
    private String finalPaymentDate;

    @Column(name = "last_deduction_date", length = 100)
    private String lastDeductionDate;

    @Column(name = "deduction_end_date", length = 100)
    private String deductionEndDate;



    @Column(name = "first_name", length = 100)
    private String firstName;

    @Column(name = "middle_name", length = 100)
    private String middleName;

    @Column(name = "last_name", length = 100)
    private String lastName;

    @Column(name = "vote_code", length = 100)
    private String voteCode;

    @Column(name = "vote_name", length = 100)
    private String voteName;

    @Column(name = "deduction_amount", length = 100)
    private BigDecimal deductionAmount;

    @Column(name = "deduction_code", length = 100)
    private String deductionCode;

    @Column(name = "deduction_name", length = 100)
    private String deductionName;

    @Column(name = "deduction_balance", length = 100)
    private BigDecimal deductionBalance;

    @Column(name = "payment_option", length = 100)
    private String paymentOption;

}