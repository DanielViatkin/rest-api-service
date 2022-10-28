package com.epam.esm.model.dto.convert;

import com.epam.esm.model.dto.OrderDto;
import com.epam.esm.model.entity.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderConverter implements DtoConvert<Order, OrderDto> {
    private final GiftCertificateConverter certificateConverter;
    private final UserConverter userConverter;

    @Autowired
    public OrderConverter(GiftCertificateConverter certificateConverter,
                          UserConverter userConverter){
        this.certificateConverter = certificateConverter;
        this.userConverter = userConverter;
    }

    @Override
    public Order convertToEntity(OrderDto dto) {
        Order order = new Order();
        order.setId(dto.getId());
        order.setCost(dto.getCost());
        order.setPurchaseTime(dto.getPurchaseTime());
        order.setCertificate(certificateConverter.convertToEntity(dto.getCertificate()));
        order.setUser(userConverter.convertToEntity(dto.getUser()));
        return order;
    }

    @Override
    public OrderDto convertToDto(Order entity) {
        OrderDto orderDto = new OrderDto();
        orderDto.setId(entity.getId());
        orderDto.setCost(entity.getCost());
        orderDto.setPurchaseTime(entity.getPurchaseTime());
        orderDto.setCertificate(certificateConverter.convertToDto(entity.getCertificate()));
        orderDto.setUser(userConverter.convertToDto(entity.getUser()));
        return orderDto;
    }

}
