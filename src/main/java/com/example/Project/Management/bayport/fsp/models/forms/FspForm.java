package com.dflex.emkopo.app.fsp.models.forms;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * Financial Service Provider Form
 * @author Yogona
 */
@Data
public class FspForm {
    @NotBlank @NotEmpty @NotNull
    @Size(max = 10)
    private String code;

    @NotBlank @NotEmpty @NotNull
    @Size(max = 100)
    private String name;

    @NotBlank @NotEmpty @NotNull
    @Size(max = 50)
    private String shortName;

    @NotBlank @NotEmpty @NotNull
    @Size(max = 50)
    private String referenceNumber;

    private Long fspCategoryId;
}
