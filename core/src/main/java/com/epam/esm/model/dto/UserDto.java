package com.epam.esm.model.dto;

import com.epam.esm.model.entity.Order;
import com.epam.esm.model.entity.Role;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

import java.util.Set;


@Data
@AllArgsConstructor
public class UserDto extends RepresentationModel<UserDto> {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;
    private String login;
    private String status;
    private Set<RoleDto> roles;
    @JsonIgnore
    private Set<OrderDto> orders;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @JsonCreator
    public UserDto(){
    }
}
