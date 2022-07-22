package com.navi.stockexchange.controller;

import com.navi.stockexchange.dto.OrderResponse;
import com.navi.stockexchange.entity.BuyOrderSet;
import com.navi.stockexchange.entity.SellOrderSet;
import com.navi.stockexchange.repository.IOrderRepository;
import com.navi.stockexchange.repository.OrderRepository;
import com.navi.stockexchange.service.IStockExchangeService;
import com.navi.stockexchange.service.StockExchangeService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1")
public class APIController {

    private final IOrderRepository orderRepository;
    private final IStockExchangeService stockExchangeService;

    public APIController(OrderRepository orderRepository, StockExchangeService stockExchangeService) {
        this.orderRepository = orderRepository;
        this.stockExchangeService = stockExchangeService;
    }

    @GetMapping("/getAllBuysStocks")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "The Get call is Successful"),
            @ApiResponse(code = 500, message = "The Get call is Failed"),
            @ApiResponse(code = 404, message = "The API could not be found") })
    public Map<String, BuyOrderSet> getAllBuysStocks(){
        return orderRepository.getBuys();
    }

    @GetMapping("/getAllSellsStocks")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "The Get call is Successful"),
            @ApiResponse(code = 500, message = "The Get call is Failed"),
            @ApiResponse(code = 404, message = "The API could not be found") })
    public Map<String, SellOrderSet> getAllSellsStocks(){
        return orderRepository.getSells();
    }

    @GetMapping("/getAllTransactions")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "The Get call is Successful"),
            @ApiResponse(code = 500, message = "The Get call is Failed"),
            @ApiResponse(code = 404, message = "The API could not be found") })
    public List<OrderResponse> getAllTransactions(){
        return orderRepository.getTransactionList();
    }

}
