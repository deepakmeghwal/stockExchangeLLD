package com.navi.stockexchange.service;

import com.navi.stockexchange.dto.OrderResponse;
import com.navi.stockexchange.entity.Order;

import java.util.List;

public interface IStockExchangeService {
    List<Order> readInputFromCommandLine();

    List<Order> readInputFromTxtFile();

    void printResult(List<OrderResponse> orderResponseList);

    Order convertTextLineToOrder(String inputOrderLine);
}
