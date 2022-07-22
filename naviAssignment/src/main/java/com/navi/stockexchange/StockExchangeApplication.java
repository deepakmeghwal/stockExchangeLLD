package com.navi.stockexchange;

import com.navi.stockexchange.service.StockManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.navi.stockexchange"})
public class StockExchangeApplication {

	public static void main(String[] args) {
		SpringApplication.run(StockExchangeApplication.class, args);
		System.out.println("Stock Exchange service is up and running\n");

		StockManager stockManager = new StockManager();
		stockManager.startStockManager();

	}

}
