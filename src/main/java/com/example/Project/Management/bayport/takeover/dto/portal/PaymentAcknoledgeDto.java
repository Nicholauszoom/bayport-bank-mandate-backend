package com.example.Project.Management.bayport.takeover.dto.portal;

import com.example.Project.Management.bayport.common.enumerator.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentAcknoledgeDto {
    private String remarks;
    private PaymentStatus paymentStatus;
}
