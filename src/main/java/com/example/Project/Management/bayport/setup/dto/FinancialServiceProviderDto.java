package com.dflex.emkopo.setup.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class FinancialServiceProviderDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String fspUid;
    private String fspCode;
    private String fspName;
    private String fspShortName;
    private String fspReferenceNumber;
    private Long recordStatusId;

    public FinancialServiceProviderDto(Long id, String fspCode, String fspName, String fspShortName, String fspReferenceNumber, Long recordStatusId, String fspUid) {
        this.id = id;
        this.fspCode = fspCode;
        this.fspName = fspName;
        this.fspShortName = fspShortName;
        this.fspReferenceNumber = fspReferenceNumber;
        this.recordStatusId = recordStatusId;
        this.fspUid = fspUid;
    }
}
