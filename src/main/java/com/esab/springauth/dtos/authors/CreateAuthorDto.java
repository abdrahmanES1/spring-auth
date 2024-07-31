
package com.esab.springauth.dtos.authors;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class CreateAuthorDto {
    String name;
    @NotEmpty(message = "nationality shouldn't be empty")
    String nationality;
}
