package com.example.Project.Management.bayport.setup.entity;

import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name = "tab_currency")
public class Currency implements Serializable {
  
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "currency_id_generator")
  @SequenceGenerator(name = "currency_id_generator", sequenceName = "seq_currency", initialValue = 1, allocationSize = 1)
  @Column(name = "id", nullable = false, precision = 11)
  private Long id;

  @Column(name = "currency_code", length = 3,nullable=false)
  private String currencyCode;
  
  @Column(name = "currency_name", length = 50,nullable=false)
  private String currencyName;
  
  @Column(name="record_status_id", nullable=false)
  private Long recordStatusId = 1L;
}