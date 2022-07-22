package com.navi.stockexchange.dto;

import com.navi.stockexchange.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderResponse {
    private UUID id;
    private Order buyOrder;
    private Order sellOrder;
    private int quantity;
    private BigDecimal price;

}
