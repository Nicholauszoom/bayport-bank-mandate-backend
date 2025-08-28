package com.example.Project.Management.bayport.balance.entity;

import com.example.Project.Management.bayport.common.entity.CommonEntity;
import com.example.Project.Management.bayport.common.enumerator.LoanType;
import com.example.Project.Management.bayport.common.enumerator.RequestType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "balance")
@Builder
public class Balance extends CommonEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "balance_id_generator")
    @SequenceGenerator(name = "balance_id_generator", sequenceName = "seq_balance", initialValue = 1, allocationSize = 1)
    @Column(name = "id", nullable = false, precision = 11)
    private Long id;

    @Column(name = "check_number", nullable = false)
    private String checkNumber;

    @Column(name = "loan_number")
    private String loanNumber;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "middle_name")
    private String middleName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "vote_code")
    private String voteCode;

    @Column(name = "vote_name")
    private String voteName;

    @Column(name = "deduction_amount")
    private BigDecimal deductionAmount;

    @Column(name = "deduction_code")
    private String deductionCode;

    @Column(name = "deduction_name")
    private String deductionName;

    @Column(name = "deduction_balance")
    private BigDecimal deductionBalance;

    @Column(name = "payment_option")
    private String paymentOption;

    // response
    @Column(name = "fsp_reference_number")
    private String fSPReferenceNumber;

    @Column(name = "payment_reference_number")
    private String paymentReferenceNumber;

    @Column(name = "total_payoff_amount")
    private BigDecimal totalPayoffAmount;

    @Column(name = "outstanding_balance")
    private BigDecimal outstandingBalance;

    @Column(name = "final_payment_date")
    private String finalPaymentDate;

    @Column(name = "last_deduction_date")
    private String lastDeductionDate;

    @Column(name = "last_pay_date")
    private String lastPayDate;

    @Column(name = "end_date")
    private String endDate;

    @Column(name = "loan_type")
    @Enumerated(EnumType.STRING)
    private LoanType loanType;

}
