package com.navi.stockexchange.service;

import com.navi.stockexchange.adapter.OrderInputStore;
import com.navi.stockexchange.adapter.OrderOutputStore;
import com.navi.stockexchange.controller.CommandLineController;
import com.navi.stockexchange.entity.Order;
import com.navi.stockexchange.repository.OrderRepository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class StockManager {
    // initialize order Repository
    OrderRepository orderRepository = new OrderRepository(OrderInputStore.getInstance(), OrderOutputStore.getInstance());

    StockExchangeService stockExchangeService = new StockExchangeService();

    public void startStockManager(){
        CommandLineController controller = new CommandLineController(orderRepository, stockExchangeService);

        controller.startReadingDataFromTxtFile();

        boolean isTradeStop = false;
        while(!isTradeStop){
            controller.startReadingDataFromCommandLine();
            System.out.println("\nType 1 and then press enter to continue with your trading  ------------------------->>>>>>>>>>>>>>>>>>>>>>>>>>> ");
            System.out.println("To Stop the Trade type any other keys and then press enter  ------------------------->>>>>>>>>>>>>>>>>>>>>>>>>>> ");
            BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
            try {
                String text = null;
                if (!(text = input.readLine()).equals("1")) {
                    isTradeStop = true;
                    break;
                }
            } catch (Exception e){
                System.out.println("startStockManager(), Exception : " + e.getMessage());
            }
        }

        System.out.println("You trade has been stopped  ------------------------->>>>>>>>>>>>>>>>>>>>>>>>>>> ");

    }

}
