package com.epam.esm.controller;

import com.epam.esm.model.dto.AuthenticationDto;
import com.epam.esm.model.dto.ChangePasswordDto;
import com.epam.esm.model.dto.JwtDto;
import com.epam.esm.model.dto.UserDto;
import com.epam.esm.model.dto.convert.UserConverter;
import com.epam.esm.model.entity.User;
import com.epam.esm.security.provider.JwtTokenProvider;
import com.epam.esm.service.logic.user.UserService;
import org.hibernate.sql.OracleJoinFragment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@RestController
@RequestMapping(path = "/auth")
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtTokenProvider provider;
    private final UserConverter userConverter;

    @Autowired
    public AuthenticationController(AuthenticationManager authenticationManager,
                                    UserService userService,
                                    JwtTokenProvider provider,
                                    UserConverter userConverter){
        this. authenticationManager = authenticationManager;
        this.userService = userService;
        this.provider = provider;
        this.userConverter = userConverter;
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public JwtDto authenticate(@RequestBody AuthenticationDto request){
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getLogin(), request.getPassword()));
        User user = userService.getByLogin(request.getLogin());
        String token = provider.createToken(request.getLogin(), user.getRolesNames());
        return new JwtDto(request.getLogin(), token);
    }

    @PostMapping("/logout")
    public void logout(ServletRequest request, ServletResponse response){
        SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler();
        securityContextLogoutHandler.logout((HttpServletRequest) request,(HttpServletResponse)  response, null);
    }

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.OK)
    public UserDto signup(@RequestBody @Validated UserDto userDto){
        User user = userConverter.convertToEntity(userDto);
        user = userService.create(user);
        userDto = userConverter.convertToDto(user);
        return userDto;
    }

    @PostMapping(value = "/change-password")
    @ResponseStatus(HttpStatus.OK)
    public CollectionModel<Void> changePassword(@RequestBody ChangePasswordDto changePasswordDto) {
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println("login     :" + login);
        userService.changePassword(login,
                changePasswordDto.getCurrentPassword(), changePasswordDto.getNewPassword());
        return CollectionModel.empty();
    }
}
