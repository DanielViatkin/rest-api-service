package com.epam.esm.model;

import lombok.*;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MostUsedTag {
    private Long id;
    private String tagName;
    private BigDecimal highestCoast;
}
