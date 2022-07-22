package com.navi.stockexchange.repository;

import com.navi.stockexchange.dto.OrderResponse;
import com.navi.stockexchange.entity.BuyOrderSet;
import com.navi.stockexchange.entity.Order;
import com.navi.stockexchange.entity.SellOrderSet;
import com.navi.stockexchange.entity.Stock;

import java.util.List;
import java.util.Map;

public interface IOrderRepository {

    Map<String, BuyOrderSet> getBuys();

    Map<String, SellOrderSet> getSells();

    List<OrderResponse> getTransactionList();

    void addOrders(List<Order> orderList) throws Exception;

    void resetOrderRepository();

    List<OrderResponse> processOrders();

}
