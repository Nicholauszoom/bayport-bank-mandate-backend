package com.example.Project.Management.bayport.setting;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "setting")
@Builder
public class Setting {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "[setting_id_generator")
    @SequenceGenerator(name = "setting_id_generator", sequenceName = "seq_setting", initialValue = 1, allocationSize = 1)
    @Column(name = "id", nullable = false, precision = 11)
    private Long id;

    @Column(name = "check_sum", nullable = false)
    private String checksum;

}
