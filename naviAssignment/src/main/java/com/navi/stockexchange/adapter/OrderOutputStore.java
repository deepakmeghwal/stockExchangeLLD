package com.navi.stockexchange.adapter;

import com.navi.stockexchange.dto.OrderResponse;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Create the store for output/result/tradeTransactions data
 * @List<OrderResponse> : orderResponseList *
 */
@Component
public final class OrderOutputStore implements IOrderOutputStore{

    private final List<OrderResponse> orderResponseList;

    public OrderOutputStore() {
        this.orderResponseList = new ArrayList<>();
    }

    private static class LazyHolder {
        static final OrderOutputStore INSTANCE = new OrderOutputStore();
    }

    public static OrderOutputStore getInstance() {
        return LazyHolder.INSTANCE;
    }

    @Override
    public List<OrderResponse> getAllOrderTransactions() {
        return getInstance().orderResponseList;
    }
}
