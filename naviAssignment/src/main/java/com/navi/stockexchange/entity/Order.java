package com.navi.stockexchange.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalTime;

@Data
public class Order {
    private final String id;
    private final LocalTime time;
    private final OrderType orderType;
    private final Stock stock;
    private int quantity;
    private final BigDecimal price;

    public Order(String id, LocalTime time, OrderType orderType, int quantity, Stock stock, BigDecimal price) {
        this.id = id;
        this.time = time;
        this.orderType = orderType;
        this.quantity = quantity;
        this.stock = stock;
        this.price = price;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof Order) && (this.id.equals(((Order) obj).getId()));
    }
}
