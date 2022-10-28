package com.epam.esm.security;

import com.epam.esm.model.security.Role;
import org.springframework.security.core.userdetails.User;
import com.epam.esm.model.security.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@AllArgsConstructor
public class SecurityUser implements UserDetails {

    private final String username;
    private final String password;
    private final List<SimpleGrantedAuthority> authorities;
    private final boolean isActive;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return isActive;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isActive;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isActive;
    }

    @Override
    public boolean isEnabled() {
        return isActive;
    }

    public static UserDetails fromUser(com.epam.esm.model.entity.User user){
        return new User(
                user.getLogin(), user.getPassword(),
                user.getStatus().equals(Status.ACTIVE.name()),
                user.getStatus().equals(Status.ACTIVE.name()),
                user.getStatus().equals(Status.ACTIVE.name()),
                user.getStatus().equals(Status.ACTIVE.name()),
                getAuthoritiesFromUserModel(user)
        );

    }

    private static List<GrantedAuthority> getAuthoritiesFromUserModel(com.epam.esm.model.entity.User user){
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (com.epam.esm.model.entity.Role roleModel : user.getRoles()){
            Role role = Role.valueOf(roleModel.getName());
            authorities.addAll(role.getAuthorities());
        }
        return authorities;
    }
}
