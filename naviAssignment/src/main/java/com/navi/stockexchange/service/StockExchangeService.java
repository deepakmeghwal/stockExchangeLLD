package com.navi.stockexchange.service;

import com.google.common.base.Splitter;
import com.navi.stockexchange.dto.OrderResponse;
import com.navi.stockexchange.entity.Order;
import com.navi.stockexchange.entity.OrderType;
import com.navi.stockexchange.entity.Stock;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

@Service
public class StockExchangeService implements IStockExchangeService{

    public List<Order> readInputFromCommandLine() {
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        List<Order> orders = new ArrayList<>();
        try {
            String textLine = null;
            while (!(textLine = input.readLine()).equals("END")) {
                orders.add(convertTextLineToOrder(textLine));
            }
        } catch (NumberFormatException | DateTimeParseException e){
            System.out.println("Input format is not valid format! Exception : " + e.getMessage());
        }catch (Exception e){
            System.out.println("Exception occur while reading input data, Exception : " + e.getMessage());
        }

        return orders;
    }

    public List<Order> readInputFromTxtFile() {
        System.out.println("Start reading data From Txt File --------------------------------->>>>>>>>>>>>>>>> ");
        List<Order> orders = new ArrayList<>();
        try {
            //creates a new file instance
            File file = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + "templates/inputData.txt");
            FileReader fr = new FileReader(file);   //reads the file
            BufferedReader br = new BufferedReader(fr);  //creates a buffering character input stream
            StringBuffer sb = new StringBuffer();    //constructs a string buffer with no characters
            String line;
            while((line=br.readLine())!=null)
            {
                sb.append(line);      //appends line to string buffer
                sb.append("\n");     //line feed
                orders.add(convertTextLineToOrder(line));
            }
            fr.close();    //closes the stream and release the resources
            System.out.println("Contents of File: ------------------------->>>>>>>>>>>>>>>>>>>>>>>>>>> ");
            System.out.print(sb.toString());   //returns a string that textually represents the object

        } catch (NumberFormatException | DateTimeParseException e){
            System.out.println("Input format is not valid format! Exception : " + e.getMessage());
        }catch (Exception e){
            System.out.println("Exception occur while reading input data, Exception : " + e.getMessage());
        }

        return orders;
    }

    public void printResult(List<OrderResponse> orderResponseList) {
        System.out.println("\nResult of Trade: ------------------------->>>>>>>>>>>>>>>>>>>>>>>>>>> ");
        System.out.println("Output format: <buy-order-id> <sell-price> <qty> <sell-order-id>");

        orderResponseList.stream().forEach((orderResponse) -> {
            String price = String.format("%.2f",orderResponse.getPrice());
            System.out.println(orderResponse.getBuyOrder().getId()+" "+price+" "+orderResponse.getQuantity()+" "+orderResponse.getSellOrder().getId());
        });
    }


    public Order convertTextLineToOrder(String inputOrderLine) {
        Splitter splitter = Splitter.on(' ').omitEmptyStrings().trimResults();
        Iterator<String> inputOrderLineItr = splitter.split(inputOrderLine).iterator();

        String orderId = inputOrderLineItr.next();

        String timeString = inputOrderLineItr.next();
        LocalTime orderTime = LocalTime.parse(timeString, DateTimeFormatter.ofPattern("HH:mm", Locale.getDefault()));

        String stockName = inputOrderLineItr.next();
        Stock stock = new Stock(stockName);

        String typeString = inputOrderLineItr.next();
        OrderType orderType = OrderType.valueOf(typeString.toUpperCase());

        BigDecimal price = new BigDecimal(inputOrderLineItr.next());
        int quantity = Integer.parseInt(inputOrderLineItr.next());

        return new Order (orderId, orderTime, orderType, quantity, stock, price);
    }

}
