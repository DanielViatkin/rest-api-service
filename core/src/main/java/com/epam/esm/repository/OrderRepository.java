package com.epam.esm.repository;

import com.epam.esm.model.entity.Order;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderRepository extends AbstractRepository<Order> {
    List<Order> getAllByUserId(long userId, Pageable pageable);
}
