package com.example.Project.Management.bayport.takeover.entity;

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
@Table(name = "tab_tekeover_balance")
public class Takeoverbal {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "takeover_balance_id_generator")
    @SequenceGenerator(name = "takeover_balance_id_generator", sequenceName = "seq_takeover_balance", initialValue = 1, allocationSize = 1)
    @Column(name = "id", nullable = false, precision = 11)
    private Long id;

    @Column(name = "loan_number", length = 50, nullable = false)
    private String loanNumber;

    @Column(name = "total_payoff_amount", length = 50, nullable = false)
    private String totalPayOffAmount;

    @Column(name = "final_payment_date", length = 50, nullable = false)
    private String finalPaymentDate;

//    @Column(name = "request_date", length = 50, nullable = false)
//    private String requestDate;



}
