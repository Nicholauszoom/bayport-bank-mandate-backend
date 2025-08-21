package com.example.Project.Management.bayport.setup.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
public class RoleDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    private String roleUid;

    private String roleName;

    private String roleDescription;

    private Long roleCategoryId;

    private String roleCategory;

    private List<Long> permissions;

}
