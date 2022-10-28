package com.epam.esm.model.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChangePasswordDto {
    private String currentPassword;
    private String newPassword;

    @JsonCreator
    public ChangePasswordDto(){

    }
}
