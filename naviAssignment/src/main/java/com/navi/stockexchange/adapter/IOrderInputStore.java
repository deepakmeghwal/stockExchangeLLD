package com.navi.stockexchange.adapter;

import com.navi.stockexchange.entity.BuyOrderSet;
import com.navi.stockexchange.entity.SellOrderSet;
import com.navi.stockexchange.entity.Stock;

import java.util.Map;

public interface IOrderInputStore {
    Map<Stock, BuyOrderSet> getAllBuyOrders();

    Map<Stock, SellOrderSet> getAllSellOrders();
}
