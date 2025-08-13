package com.dflex.emkopo.app.setup.entity;

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
@Table(name = "tab_fsp_dflex_response")
public class FSPDflexResponse {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fsp_dflex_response_id_generator")
    @SequenceGenerator(name = "fsp_dflex_response__id_generator", sequenceName = "seq_fsp_dflex_response", initialValue = 1, allocationSize = 1)
    @Column(name = "id", nullable = false, precision = 11)
    private Long id;

    @Column(name = "response_code", length = 12,nullable=false)
    private String responseCode;

    @Column(name = "response_description", length = 50,nullable=false)
    private String responseDescription;

    @Column(name="response_receive_date", nullable=false)
    private Date responseReceivedDate;

}
