package com.epam.esm.model.dto;

import com.epam.esm.model.dto.convert.DtoConvert;
import com.epam.esm.model.entity.Role;
import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class RoleDto {
    private Long id;
    private String name;

    @JsonCreator
    public RoleDto(){
    }
}
