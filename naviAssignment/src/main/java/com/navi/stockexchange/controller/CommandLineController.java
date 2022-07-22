package com.navi.stockexchange.controller;

import com.navi.stockexchange.exception.OrderException;
import com.navi.stockexchange.repository.IOrderRepository;
import com.navi.stockexchange.repository.OrderRepository;
import com.navi.stockexchange.service.IStockExchangeService;
import com.navi.stockexchange.service.StockExchangeService;


/**
 * This controller is for Command Line Controller, take the input from CLI or text file.
 */
public class CommandLineController {

    private final IOrderRepository orderRepository;
    private final IStockExchangeService stockExchangeService;

    public CommandLineController(OrderRepository orderRepository, StockExchangeService stockExchangeService) {
        this.orderRepository = orderRepository;
        this.stockExchangeService = stockExchangeService;
    }

    /**
     * Use this method for start Reading Data From Command Line
     * @param
     * @return : void
     */
    public void startReadingDataFromCommandLine() {
        try {
            orderRepository.resetOrderRepository();
            System.out.println("\nEnter command line input ------------------------->>>>>>>>>>>>>>>>>>>>>>>>>>> ");
            System.out.println("Input Format : <order-id> <time> <stock> <buy/sell> <price> <qty>,  type END to finish input");

            orderRepository.addOrders(stockExchangeService.readInputFromCommandLine());

            stockExchangeService.printResult(orderRepository.processOrders());
        } catch (OrderException e) {
            System.out.println("Exception occur, input is not valid, Exception : " + e.getMessage());
        } catch (Exception e) {
            System.out.println("startReadingDataFromCommandLine() : Exception occur, ex : " + e.getMessage());
        }
    }

    /**
     * Use this method for start Reading Data From Txt File
     * @param
     * @return : void
     */
    public void startReadingDataFromTxtFile() {
        try {
            orderRepository.resetOrderRepository();
            orderRepository.addOrders(stockExchangeService.readInputFromTxtFile());

            stockExchangeService.printResult(orderRepository.processOrders());
        } catch (OrderException e) {
            System.out.println("Exception occur, input is not valid, Exception : " + e.getMessage());
        } catch (Exception e) {
            System.out.println("startReadingDataFromTxtFile() : Exception occur, ex : " + e.getMessage());
        }
    }

}
