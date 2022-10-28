package com.epam.esm.model.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthenticationDto {
    private String login;
    private String password;

    @JsonCreator
    public AuthenticationDto(){

    }
}
