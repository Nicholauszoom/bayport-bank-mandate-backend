package com.example.Project.Management.bayport.auth.user.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public class UserDto implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long id;

    private String role;

    private String password;

    private String email;

    private String fullName;

    private String userName;




}
