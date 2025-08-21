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
@Table(name = "price")
public class Price {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "price_id_generator")
    @SequenceGenerator(name = "price_id_generator", sequenceName = "seq_price", initialValue = 1, allocationSize = 1)
    @Column(name = "id", nullable = false, precision = 11)
    private Long id;

    @Column(name = "product_name", length = 255)
    private String productName;

    @Column(name = "start_amount", length = 255)
    private BigDecimal startAmount;

    @Column(name = "end_amount", length = 255)
    private BigDecimal endAmount;

    @Column(name = "increment", length = 255)
    private double increment;

    @Column(name = "interest_rate", length = 255)
    private BigDecimal interestRate;

    @Column(name = "loan_term", length = 255)
    private int loanTerm;

    @Column(name = "description", length = 255)
    private String description;

    @Column(name = "admin_percentage", length = 255)
    private Double adminPercentage;

    @Column(name = "product_class_description", length = 255)
    private String productClassDescription;

    @Column(name = "insurance_percentage", length = 255)
    private Double insurancePercentage;

    @Column(name = "annual_interest_rate", length = 255)
    private Double annualInterestRate;

    @Column(name = "tcoc-cpp", length = 255)
    private Double tcocCpp;

    @Column(name = "factor", length = 255)
    private Double factor;
}
