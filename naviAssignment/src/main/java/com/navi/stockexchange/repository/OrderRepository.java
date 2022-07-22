package com.navi.stockexchange.repository;

import com.navi.stockexchange.adapter.OrderInputStore;
import com.navi.stockexchange.adapter.OrderOutputStore;
import com.navi.stockexchange.dto.OrderResponse;
import com.navi.stockexchange.entity.*;
import com.navi.stockexchange.exception.OrderException;
import org.springframework.stereotype.Component;

import java.util.*;


/**
 * This is Order Repository to track the transaction and trades
 * @BuyOrderSet
 * @SellOrderSet
 * @OrderResponse
 */
@Component
public class OrderRepository implements IOrderRepository{
    private final Map<Stock, BuyOrderSet> buys;
    private final Map<Stock, SellOrderSet> sells;
    private final List<OrderResponse> transactionList;

    public OrderRepository(OrderInputStore orderInputStore, OrderOutputStore orderOutputStore) {
        this.buys = orderInputStore.getAllBuyOrders();
        this.sells = orderInputStore.getAllSellOrders();
        this.transactionList = orderOutputStore.getAllOrderTransactions();
    }

    public void resetOrderRepository() {
        //System.out.println("\nresetting Order Repository --------------------------------->>>>>>>>>>>>>>>>\n");
        buys.clear();
        sells.clear();
        transactionList.clear();
    }

    public Map<String, BuyOrderSet> getBuys() {

        Map<String, BuyOrderSet>result = new HashMap<>();
        buys.forEach((k,v)->{
            result.put(k.getName(),v);
        });

        return result;
    }

    public Map<String, SellOrderSet> getSells() {
        Map<String, SellOrderSet>result = new HashMap<>();
        sells.forEach((k,v)->{
            result.put(k.getName(),v);
        });
        return result;
    }

    public List<OrderResponse> getTransactionList() {
        return transactionList;
    }

    /**
     * add Orders in repository
     * @param orderList
     * @return : void
     */
    @Override
    public void addOrders(List<Order> orderList) throws OrderException {
        if (orderList == null || orderList.isEmpty()) {
            return;
        }

        for (Order order : orderList) {
            if (order == null) {
                continue;
            }

            if (order.getStock() == null) {
                throw new OrderException("No stocks attached to Order: " + order.getId());
            }

            Set<Order> orderSet = null;
            if (order.getOrderType() == OrderType.BUY) {
                BuyOrderSet buyOrders = buys.get(order.getStock());
                if (buyOrders == null) {
                    buyOrders = new BuyOrderSet();
                    buys.put(order.getStock(), buyOrders);
                }
                orderSet = buyOrders.getOrderSet();
            } else if (order.getOrderType() == OrderType.SELL) {
                SellOrderSet sellOrders = sells.get(order.getStock());
                if (sellOrders == null) {
                    sellOrders = new SellOrderSet();
                    sells.put(order.getStock(), sellOrders);
                }
                orderSet = sellOrders.getOrderSet();
            }

            if (orderSet.contains(order)) {
                throw new OrderException("Order is possibly duplicated: " + order.getId());
            } else {
                orderSet.add(order);
            }
        }
    }

    /**
     * process Orders
     * @return : List<OrderResponse>
     */
    @Override
    public List<OrderResponse> processOrders() {
        if (buys == null || buys.isEmpty() || sells == null || sells.isEmpty()) {
            return transactionList;
        }

        // process buy orders
        buys.forEach((stock, orders) -> {
            Set<Order> buyOrderSet = orders.getOrderSet();

            if (buyOrderSet == null || buyOrderSet.isEmpty()) {
                return;
            }

            SellOrderSet sOrderSet = sells.get(stock);
            if (sOrderSet == null) {
                return;
            }

            Set<Order> sellOrderSet = sOrderSet.getOrderSet();

            buyOrderSet.stream().filter(order -> (order.getQuantity() > 0)).forEach((buy) -> {
                for (Order sell : sellOrderSet) {
                    if (buy.getQuantity() > 0  && sell.getQuantity() > 0 && buy.getPrice().compareTo(sell.getPrice()) >= 0) {

                        int qty = 0;
                        if (sell.getQuantity() > buy.getQuantity()) {
                            qty = buy.getQuantity();
                            sell.setQuantity(sell.getQuantity() - buy.getQuantity());
                            buy.setQuantity(0);
                        } else {
                            qty = sell.getQuantity();
                            buy.setQuantity(buy.getQuantity() - sell.getQuantity());
                            sell.setQuantity(0);
                        }

                        // record it in order entry
                        OrderResponse orderResponse = new OrderResponse().builder()
                                .id(UUID.randomUUID())
                                .buyOrder(buy)
                                .sellOrder(sell)
                                .quantity(qty)
                                .price(sell.getPrice())
                                .build();

                        transactionList.add(orderResponse);
                    }
                }
            });
        });

        return transactionList;
    }
}
