package com.epam.esm.model.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JwtDto {
    private String login;
    private String token;

    @JsonCreator
    public JwtDto(){
    }
}
