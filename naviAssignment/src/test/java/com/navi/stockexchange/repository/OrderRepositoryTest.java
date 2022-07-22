package com.navi.stockexchange.repository;

import com.navi.stockexchange.adapter.OrderInputStore;
import com.navi.stockexchange.adapter.OrderOutputStore;
import com.navi.stockexchange.dto.OrderResponse;
import com.navi.stockexchange.entity.*;
import com.navi.stockexchange.exception.OrderException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class OrderRepositoryTest {

    private OrderRepository orderRepository;
    private Map<Stock, BuyOrderSet> buys;
    private Map<Stock, SellOrderSet> sells;

    @BeforeEach
    void instantiateRepo() {
        this.orderRepository = new OrderRepository(OrderInputStore.getInstance(), OrderOutputStore.getInstance());
        this.buys = OrderInputStore.getInstance().getAllBuyOrders();
        this.sells = OrderInputStore.getInstance().getAllSellOrders();
    }

    @AfterEach
    void cleanup() {
        this.orderRepository.resetOrderRepository();
    }

    @Test
    @DisplayName("add Valid Orders")
    void addValidOrders() {
        Stock test = new Stock("test");

        List<Order> orders = new ArrayList<>();
        Order o1 = new Order("#100", LocalTime.parse("11:01:02"), OrderType.BUY, 52, test, new BigDecimal("10.20"));
        orders.add(o1);

        Order o2 = new Order("#200", LocalTime.parse("07:45:00"), OrderType.SELL, 87, test, new BigDecimal("47.58"));
        orders.add(o2);

        assertDoesNotThrow(() -> {
            orderRepository.addOrders(orders);
        });

        assertEquals(buys.get(test).getOrderSet().size(), 1);
        assertEquals(sells.get(test).getOrderSet().size(), 1);
        assertTrue(buys.get(test).getOrderSet().contains(o1));
        assertTrue(sells.get(test).getOrderSet().contains(o2));
    }

    @Test
    @DisplayName("add Invalid Orders")
    void addInvalidOrders() {
        List<Order> orders = new ArrayList<>();
        Order o = new Order("#100", LocalTime.parse("11:01:02"), OrderType.BUY, 52, null, new BigDecimal("10.20"));
        orders.add(o);

        assertThrows(OrderException.class, () -> {
            orderRepository.addOrders(orders);
        });

        assertTrue(buys.isEmpty());
        assertTrue(sells.isEmpty());
    }

    @Test
    @DisplayName("add Duplicate Orders")
    void addDuplicateOrders() {
        Stock test = new Stock("test");

        List<Order> orders = new ArrayList<>();
        Order o1 = new Order("#100", LocalTime.parse("11:01:02"), OrderType.BUY, 52, test, new BigDecimal("10.20"));
        orders.add(o1);

        Order o2 = new Order("#100", LocalTime.parse("11:01:02"), OrderType.BUY, 52, test, new BigDecimal("10.20"));
        orders.add(o2);

        assertThrows(OrderException.class, () -> {
            orderRepository.addOrders(orders);
        });

        assertEquals(buys.get(test).getOrderSet().size(), 1);
        assertNull(sells.get(test));
    }

    @Test
    @DisplayName("add Empty Order")
    void addEmptyOrder() {
        List<Order> orders = new ArrayList<>();

        assertDoesNotThrow(() -> {
            orderRepository.addOrders(orders);
        });

        assertTrue(buys.isEmpty());
        assertTrue(sells.isEmpty());
    }

    @Test
    @DisplayName("test Reset Order Repository")
    void testResetOrderRepository() {
        Stock test = new Stock("test");
        BuyOrderSet buy = new BuyOrderSet();
        buys.put(test, buy);

        Order o = new Order("#100", LocalTime.parse("11:01:02"), OrderType.BUY, 52, test, new BigDecimal("10.20"));
        buy.getOrderSet().add(o);

        SellOrderSet sell = new SellOrderSet();
        sells.put(test, sell);

        Order s = new Order("#200", LocalTime.parse("07:45:00"), OrderType.SELL, 87, test, new BigDecimal("47.58"));
        sell.getOrderSet().add(s);

        orderRepository.resetOrderRepository();

        assertTrue(buys.isEmpty());
        assertTrue(sells.isEmpty());
        assertTrue(OrderOutputStore.getInstance().getAllOrderTransactions().isEmpty());
    }

    @Test
    @DisplayName("process Empty Orders")
    void processEmptyOrders() {
        List<OrderResponse> result = orderRepository.processOrders();
        assertEquals(result.size(), 0);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("process Trade Executed use case")
    void processTradeExecuted() {
        Stock test = new Stock("test");
        BuyOrderSet buy = new BuyOrderSet();
        buys.put(test, buy);

        Order o1 = new Order("#100", LocalTime.parse("11:01:02"), OrderType.BUY, 52, test, new BigDecimal("100.20"));
        buy.getOrderSet().add(o1);

        SellOrderSet sell = new SellOrderSet();
        sells.put(test, sell);

        Order o2 = new Order("#200", LocalTime.parse("07:45:00"), OrderType.SELL, 87, test, new BigDecimal("47.58"));
        sell.getOrderSet().add(o2);

        List<OrderResponse> result = orderRepository.processOrders();

        assertTrue(result.size() > 0);
        assertEquals(result.get(0).getBuyOrder().getId(), "#100");
        assertEquals(result.get(0).getSellOrder().getId(), "#200");
        assertEquals(result.get(0).getPrice().compareTo(new BigDecimal("47.58")), 0);
        assertEquals(result.get(0).getQuantity(), 52);
    }

    @Test
    @DisplayName("trade Not Executed use case")
    void tradeNotExecuted() {
        Stock test = new Stock("test");
        BuyOrderSet buy = new BuyOrderSet();
        buys.put(test, buy);

        Order o1 = new Order("#100", LocalTime.parse("11:01:02"), OrderType.BUY, 52, test, new BigDecimal("10.20"));
        buy.getOrderSet().add(o1);

        SellOrderSet sell = new SellOrderSet();
        sells.put(test, sell);

        Order o2 = new Order("#200", LocalTime.parse("07:45:00"), OrderType.SELL, 87, test, new BigDecimal("47.58"));
        sell.getOrderSet().add(o2);

        List<OrderResponse> result = orderRepository.processOrders();

        assertNotNull(result);
        assertTrue(result.size() == 0);
    }

    @Test
    @DisplayName("process Trade If Price Are Same")
    void processTradeIfPriceAreSame() {
        Stock test = new Stock("test");
        BuyOrderSet buy = new BuyOrderSet();
        buys.put(test, buy);

        Order o = new Order("#100", LocalTime.parse("10:00:00"), OrderType.BUY, 100, test, new BigDecimal("10.01"));
        buy.getOrderSet().add(o);

        SellOrderSet sell = new SellOrderSet();
        sells.put(test, sell);

        Order s = new Order("#200", LocalTime.parse("09:01:00"), OrderType.SELL, 90, test, new BigDecimal("10.01"));
        sell.getOrderSet().add(s);
        Order s2 = new Order("#300", LocalTime.parse("09:11:00"), OrderType.SELL, 100, test, new BigDecimal("10.01"));
        sell.getOrderSet().add(s2);

        List<OrderResponse> result = orderRepository.processOrders();

        assertTrue(result.size() == 2);
        assertEquals(result.get(0).getBuyOrder().getId(), "#100");
        assertEquals(result.get(0).getSellOrder().getId(), "#200");
        assertEquals(result.get(0).getPrice().compareTo(new BigDecimal("10.01")), 0);
        assertEquals(result.get(0).getQuantity(), 90);

        assertEquals(result.get(1).getBuyOrder().getId(), "#100");
        assertEquals(result.get(1).getSellOrder().getId(), "#300");
        assertEquals(result.get(1).getPrice().compareTo(new BigDecimal("10.01")), 0);
        assertEquals(result.get(1).getQuantity(), 10);
    }

    @Test
    @DisplayName("process Trade If Time Are Same use case 1")
    void processTradeIfTimeAreSame() {
        Stock test = new Stock("test");
        BuyOrderSet buy = new BuyOrderSet();
        buys.put(test, buy);

        Order o = new Order("#100", LocalTime.parse("10:00:00"), OrderType.BUY, 100, test, new BigDecimal("10.01"));
        buy.getOrderSet().add(o);

        SellOrderSet sell = new SellOrderSet();
        sells.put(test, sell);

        Order s = new Order("#200", LocalTime.parse("09:01:00"), OrderType.SELL, 90, test, new BigDecimal("9.01"));
        sell.getOrderSet().add(s);
        Order s2 = new Order("#300", LocalTime.parse("09:01:00"), OrderType.SELL, 100, test, new BigDecimal("05.01"));
        sell.getOrderSet().add(s2);

        List<OrderResponse> result = orderRepository.processOrders();

        assertTrue(result.size() == 1);
        assertEquals(result.get(0).getBuyOrder().getId(), "#100");
        assertEquals(result.get(0).getSellOrder().getId(), "#300");
        assertEquals(result.get(0).getPrice().compareTo(new BigDecimal("05.01")), 0);
        assertEquals(result.get(0).getQuantity(), 100);
    }

    @Test
    @DisplayName("process Trade If Time Are Same use case 2")
    void processTradeIfTimeAreSame2() {
        Stock test = new Stock("test");
        BuyOrderSet buy = new BuyOrderSet();
        buys.put(test, buy);

        Order o = new Order("#100", LocalTime.parse("10:00:00"), OrderType.BUY, 110, test, new BigDecimal("10.01"));
        buy.getOrderSet().add(o);

        SellOrderSet sell = new SellOrderSet();
        sells.put(test, sell);

        Order s = new Order("#200", LocalTime.parse("09:01:00"), OrderType.SELL, 90, test, new BigDecimal("9.01"));
        sell.getOrderSet().add(s);
        Order s2 = new Order("#300", LocalTime.parse("09:01:00"), OrderType.SELL, 100, test, new BigDecimal("05.01"));
        sell.getOrderSet().add(s2);

        List<OrderResponse> result = orderRepository.processOrders();

        assertTrue(result.size() == 2);
        assertEquals(result.get(0).getBuyOrder().getId(), "#100");
        assertEquals(result.get(0).getSellOrder().getId(), "#300");
        assertEquals(result.get(0).getPrice().compareTo(new BigDecimal("05.01")), 0);
        assertEquals(result.get(0).getQuantity(), 100);

        assertEquals(result.get(1).getBuyOrder().getId(), "#100");
        assertEquals(result.get(1).getSellOrder().getId(), "#200");
        assertEquals(result.get(1).getPrice().compareTo(new BigDecimal("9.01")), 0);
        assertEquals(result.get(1).getQuantity(), 10);
    }

    @Test
    @DisplayName("process Multi Stock Orders")
    void processMultiStockOrders() {
        Stock test = new Stock("test");
        Stock test1 = new Stock("test1");

        BuyOrderSet buy = new BuyOrderSet();
        BuyOrderSet buy1 = new BuyOrderSet();
        buys.put(test, buy);
        buys.put(test1, buy1);

        SellOrderSet sell = new SellOrderSet();
        sells.put(test, sell);
        SellOrderSet sell1 = new SellOrderSet();
        sells.put(test1, sell1);

        Order o = new Order("#100", LocalTime.parse("10:00:00"), OrderType.BUY, 100, test, new BigDecimal("10.01"));
        buy.getOrderSet().add(o);

        Order o1 = new Order("#100a", LocalTime.parse("10:00:00"), OrderType.BUY, 100, test1, new BigDecimal("10.01"));
        buy1.getOrderSet().add(o1);

        Order s = new Order("#200", LocalTime.parse("09:01:00"), OrderType.SELL, 90, test, new BigDecimal("10.01"));
        sell.getOrderSet().add(s);
        Order s1 = new Order("#200a", LocalTime.parse("09:01:00"), OrderType.SELL, 100, test1, new BigDecimal("9.01"));
        sell1.getOrderSet().add(s1);

        List<OrderResponse> result = orderRepository.processOrders();

        assertTrue(result.size() == 2);
        assertEquals(result.get(0).getBuyOrder().getId(), "#100");
        assertEquals(result.get(0).getSellOrder().getId(), "#200");
        assertEquals(result.get(0).getPrice().compareTo(new BigDecimal("10.01")), 0);
        assertEquals(result.get(0).getQuantity(), 90);

        assertEquals(result.get(1).getBuyOrder().getId(), "#100a");
        assertEquals(result.get(1).getSellOrder().getId(), "#200a");
        assertEquals(result.get(1).getPrice().compareTo(new BigDecimal("9.01")), 0);
        assertEquals(result.get(1).getQuantity(), 100);
    }
}