package com.epam.esm.service.logic.order;

import com.epam.esm.model.entity.Order;

import java.util.List;

public interface OrderService {
    Order create(Long certificateId, Long userId);
    Order getOrderById(long id);
    List<Order> getOrdersByUserId(long userId, int page, int size);
}
