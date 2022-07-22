package com.navi.stockexchange.adapter;

import com.navi.stockexchange.entity.BuyOrderSet;
import com.navi.stockexchange.entity.SellOrderSet;
import com.navi.stockexchange.entity.Stock;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;


/**
 * Create the store for input data
 * @BuyOrderSet : for buyMap
 * @SellOrderSet : for sellMap
 */
@Component
public final class OrderInputStore implements IOrderInputStore{
    private final Map<Stock, BuyOrderSet> buyMap;
    private final Map<Stock, SellOrderSet> sellMap;


    private OrderInputStore() {
        this.buyMap = new HashMap<>();
        this.sellMap = new HashMap<>();
    }


    private static class LazyHolder {
        static final OrderInputStore INSTANCE = new OrderInputStore();
    }

    public static OrderInputStore getInstance() {
        return LazyHolder.INSTANCE;
    }

    @Override
    public Map<Stock, BuyOrderSet> getAllBuyOrders() {
        return getInstance().buyMap;
    }

    @Override
    public Map<Stock, SellOrderSet> getAllSellOrders() {
        return getInstance().sellMap;
    }
}
