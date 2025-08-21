package com.example.Project.Management.bayport.setup.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name = "tab_utumishi_response")
public class UtumishiResponse {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "utumishi_response_id_generator")
    @SequenceGenerator(name = "utumishi_response_id_generator", sequenceName = "seq_utumishi_response", initialValue = 1, allocationSize = 1)
    @Column(name = "id", nullable = false, precision = 11)
    private Long id;

    @Column(name = "response_code", length = 12,nullable=false)
    private String responseCode;

    @Column(name = "response_description", length = 50,nullable=false)
    private String responseDescription;

    @Column(name="response_receive_date", nullable=false)
    private Date responseReceivedDate;

}
