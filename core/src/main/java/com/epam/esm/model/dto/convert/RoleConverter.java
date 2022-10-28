package com.epam.esm.model.dto.convert;

import com.epam.esm.model.dto.RoleDto;
import com.epam.esm.model.entity.Role;
import org.springframework.stereotype.Component;

@Component
public class RoleConverter implements DtoConvert<Role, RoleDto>{

    @Override
    public Role convertToEntity(RoleDto dto) {
        Role role = new Role();
        role.setId(dto.getId());
        role.setName(dto.getName());
        return role;
    }

    @Override
    public RoleDto convertToDto(Role entity) {
        RoleDto dto = new RoleDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        return dto;
    }
}
