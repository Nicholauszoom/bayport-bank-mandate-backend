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

    @Column(name = "check_number", length = 255)
    private String checkNumber;

    @Column(name = "replace_balance", length = 255)
    private BigDecimal replaceBalance;
}
