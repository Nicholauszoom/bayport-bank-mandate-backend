package com.dflex.emkopo.app.fsp.models.forms;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class FspCategoryForm {
    @NotNull @NotEmpty @NotBlank
    @Size(max = 20)
    private String code;

    @NotNull @NotEmpty @NotBlank
    @Size(max = 50)
    private String name;
}
