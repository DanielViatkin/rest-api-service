package com.epam.esm.model.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@AllArgsConstructor
public class OrderDto extends RepresentationModel<OrderDto> {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;
    private BigDecimal cost;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'hh:mm:ss.SSS", timezone = "UTC")
    private Instant purchaseTime;
    private UserDto user;
    private GiftCertificateDto certificate;

    @JsonCreator
    public OrderDto(){
    }
}
