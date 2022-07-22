package com.navi.stockexchange.adapter;

import com.navi.stockexchange.dto.OrderResponse;

import java.util.List;

public interface IOrderOutputStore {
    List<OrderResponse> getAllOrderTransactions();
}
