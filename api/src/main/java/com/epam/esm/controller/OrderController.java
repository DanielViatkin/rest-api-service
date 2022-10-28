package com.epam.esm.controller;

import com.epam.esm.link.OrderLinkBuilder;
import com.epam.esm.model.dto.OrderDto;
import com.epam.esm.model.dto.convert.OrderConverter;
import com.epam.esm.model.entity.Order;
import com.epam.esm.service.logic.order.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "/orders")
public class OrderController {
    private final OrderService orderService;
    private final OrderConverter orderConverter;
    private final OrderLinkBuilder orderLinkBuilder;
    
    @Autowired
    public OrderController(OrderService orderService,
                           OrderConverter orderConverter,
                           OrderLinkBuilder orderLinkBuilder){
        this.orderService = orderService;
        this.orderConverter = orderConverter;
        this.orderLinkBuilder = orderLinkBuilder;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('order:create')")
    public OrderDto create(@RequestParam("certificate_id") Long certificateId,
                           @RequestParam("user_id") Long userId){
        Order order = orderService.create(certificateId, userId);

        OrderDto resultOrderDto = orderConverter.convertToDto(order);
        orderLinkBuilder.addOrderRelatedLinks(resultOrderDto);
        Link selfLink = linkTo(methodOn(OrderController.class).create(certificateId, userId)).withSelfRel();
        resultOrderDto.add(selfLink);
        return resultOrderDto;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('order:get')")
    public OrderDto getOrderById(@PathVariable("id") long id){
        Order order = orderService.getOrderById(id);
        OrderDto orderDto = orderConverter.convertToDto(order);
        orderLinkBuilder.addOrderRelatedLinks(orderDto);
        return orderDto;
    }

    @GetMapping(value = "/user-orders/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, "application/hal+json"})
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('order:get')")
    public CollectionModel<OrderDto> getUserOrders(@PathVariable("id") long userId,
                                                   @RequestParam(value = "page", defaultValue = "0", required = false) int page,
                                                   @RequestParam(value = "size", defaultValue = "20", required = false) int size){
        List<Order> orders = orderService.getOrdersByUserId(userId, page, size);
        Set<OrderDto> orderDtos = new HashSet<>();
        for (Order order : orders){
            orderDtos.add(orderConverter.convertToDto(order));
        }
        orderLinkBuilder.addOrdersRelatedLinks(orderDtos);
        Link selfLink = linkTo(methodOn(OrderController.class).getUserOrders(userId, page, size)).withSelfRel();
        return CollectionModel.of(orderDtos, selfLink);
    }
}
