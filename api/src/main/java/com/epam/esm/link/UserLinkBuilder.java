package com.epam.esm.link;

import com.epam.esm.controller.UserController;
import com.epam.esm.model.dto.OrderDto;
import com.epam.esm.model.dto.UserDto;
import com.epam.esm.model.entity.Order;
import com.epam.esm.model.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import java.util.List;
import java.util.Set;

@Component
public class UserLinkBuilder {
    public void addToDoLinks(UserDto user){
        Link getLink = linkTo(methodOn(UserController.class).getById(user.getId())).withRel("get");
        user.add(getLink);
    }

    public void addUserRelatedLinks(UserDto user){
        addToDoLinks(user);
    }

    public void addUsersRelatedLinks(Set<UserDto> users){
        for (UserDto user : users){
            addUserRelatedLinks(user);
        }
    }
}
