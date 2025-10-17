package com.example.Project.Management.bayport.dashboard.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Builder
public class DashboardDto {
   private int totalUsers;
    private int totalMandates;
    private int successMandates;
    private int failedMandates;
}