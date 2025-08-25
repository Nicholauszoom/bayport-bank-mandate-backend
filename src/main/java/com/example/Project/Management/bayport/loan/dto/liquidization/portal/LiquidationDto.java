package com.example.Project.Management.bayport.loan.dto.liquidization.portal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LiquidationDto {
    private String applicationNumber;
    private String loanNumber;
    private String remarks;
}
