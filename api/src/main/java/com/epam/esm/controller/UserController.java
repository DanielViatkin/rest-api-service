package com.epam.esm.controller;

import com.epam.esm.link.OrderLinkBuilder;
import com.epam.esm.link.UserLinkBuilder;
import com.epam.esm.model.MostUsedTag;
import com.epam.esm.model.dto.ChangePasswordDto;
import com.epam.esm.model.dto.UserDto;
import com.epam.esm.model.dto.convert.UserConverter;
import com.epam.esm.model.entity.User;
import com.epam.esm.service.logic.tag.TagService;
import com.epam.esm.service.logic.user.UserService;
import com.epam.esm.service.logic.tag.TagServiceImpl;
import com.epam.esm.service.logic.user.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
@RestController
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;
    private final TagService tagService;
    private final UserLinkBuilder userLinkBuilder;
    private final UserConverter userConverter;

    @Autowired
    public UserController(UserServiceImpl userService,
                          TagServiceImpl tagService,
                          UserLinkBuilder userLinkBuilder,
                          UserConverter userConverter){
        this.userService = userService;
        this.tagService = tagService;
        this.userLinkBuilder = userLinkBuilder;
        this.userConverter = userConverter;
    }

    @GetMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('user:get')")
    public UserDto getById(@PathVariable long id){
        User user = userService.getById(id);
        UserDto userDto = userConverter.convertToDto(user);
        userLinkBuilder.addUserRelatedLinks(userDto);
        return userDto;
    }

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, "application/hal+json"})
    @ResponseStatus(HttpStatus.OK)
    public CollectionModel<UserDto> getAll(@RequestParam(value = "page", defaultValue = "0", required = false) int page,
                                           @RequestParam(value = "size", defaultValue = "20", required = false) int size){
        List<User> users = userService.getAll(page, size);
        Set<UserDto> userDtos = new HashSet<>();
        for (User user : users){
            userDtos.add(userConverter.convertToDto(user));
        }
        userLinkBuilder.addUsersRelatedLinks(userDtos);
        Link selfLink = linkTo(UserController.class).withSelfRel();
        return CollectionModel.of(userDtos, selfLink);
    }

    @GetMapping(value = "{id}/most-used-tag")
    @ResponseStatus(HttpStatus.OK)
    public MostUsedTag getMostUsedTagByUserId(@PathVariable("id") long userId){
        return tagService.getMostUsedTagByUserId(userId);
    }

}
