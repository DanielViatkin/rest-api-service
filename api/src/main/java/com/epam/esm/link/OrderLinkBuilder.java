package com.epam.esm.link;

import com.epam.esm.controller.OrderController;
import com.epam.esm.model.dto.OrderDto;
import com.epam.esm.model.entity.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class OrderLinkBuilder {
    private final GiftCertificateLinkBuilder certificateLinkBuilder;
    private final UserLinkBuilder userLinkBuilder;

    @Autowired
    public OrderLinkBuilder(GiftCertificateLinkBuilder certificateLinkBuilder,
                            UserLinkBuilder userLinkBuilder){
        this.certificateLinkBuilder = certificateLinkBuilder;
        this.userLinkBuilder = userLinkBuilder;
    }

    public void addToDoLinks(OrderDto order){
        Link createLink = linkTo(methodOn(OrderController.class).
                create(order.getCertificate().getId(), order.getUser().getId())).withRel("create");
        Link readLink = linkTo(methodOn(OrderController.class).getOrderById(order.getUser().getId())).withRel("get");
        order.add(createLink);
        order.add(readLink);
    }

    public void addOrderRelatedLinks(OrderDto orderDto){
        certificateLinkBuilder.addCertificateRelatedLinks(orderDto.getCertificate(), orderDto.getCertificate().getTags());
        userLinkBuilder.addUserRelatedLinks(orderDto.getUser());
        addToDoLinks(orderDto);
    }

    public void addOrdersRelatedLinks(Set<OrderDto> orders){
        for(OrderDto order : orders){
            addOrderRelatedLinks(order);
        }
    }
}
