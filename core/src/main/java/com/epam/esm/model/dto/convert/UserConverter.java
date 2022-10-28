package com.epam.esm.model.dto.convert;

import com.epam.esm.model.dto.OrderDto;
import com.epam.esm.model.dto.RoleDto;
import com.epam.esm.model.dto.UserDto;
import com.epam.esm.model.entity.Order;
import com.epam.esm.model.entity.Role;
import com.epam.esm.model.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;


@Component
public class UserConverter implements DtoConvert<User, UserDto> {

    private final RoleConverter roleConverter;

    @Autowired
    public UserConverter(RoleConverter roleConverter){
        this.roleConverter = roleConverter;
    }

    @Override
    public User convertToEntity(UserDto dto) {
        System.out.println("dot password " + dto.getPassword());
        User user = new User();
        user.setId(dto.getId());
        user.setLogin(dto.getLogin());
        user.setPassword(dto.getPassword());
        if (dto.getRoles() != null){
            Set<Role> roles = new HashSet<>();
            for (RoleDto roleDto : dto.getRoles()){
                roles.add(roleConverter.convertToEntity(roleDto));
            }
            user.setRoles(roles);
        }
        user.setStatus(dto.getStatus());
        System.out.println("user password " + user.getPassword());
        return user;
    }

    @Override
    public UserDto convertToDto(User entity) {
        UserDto userDto = new UserDto();
        userDto.setId(entity.getId());
        userDto.setLogin(entity.getLogin());
        userDto.setPassword(entity.getPassword());
        //TODO delete
        System.out.println(entity.getRoles().size() + " size of roles ");
        if (entity.getRoles() != null){
            Set<RoleDto> dtos = new HashSet<>();
            for (Role role : entity.getRoles()){
                dtos.add(roleConverter.convertToDto(role));
            }
            userDto.setRoles(dtos);
        }
        userDto.setStatus(entity.getStatus());
        return userDto;
    }
}
