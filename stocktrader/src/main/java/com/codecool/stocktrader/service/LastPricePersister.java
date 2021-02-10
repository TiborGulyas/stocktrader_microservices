package com.codecool.stocktrader.service;

import com.codecool.stocktrader.model.LastPrice;
import com.codecool.stocktrader.model.Stock;
import com.codecool.stocktrader.repository.LastPriceRepository;
import com.codecool.stocktrader.repository.StockRepository;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Calendar;


@Service
public class LastPricePersister {

    @Autowired
    StockRepository stockRepository;

    @Autowired
    LastPriceRepository lastPriceRepository;

    public void persistCurrentPrice(JsonObject response, String symbol){
        float currentPrice = NumberRounder.roundFloat(response.getAsJsonPrimitive("c").getAsFloat(), 2);
        float openPrice =  NumberRounder.roundFloat(response.getAsJsonPrimitive("o").getAsFloat(),2);
        float highPrice =  NumberRounder.roundFloat(response.getAsJsonPrimitive("h").getAsFloat(),2);
        float lowPrice =  NumberRounder.roundFloat(response.getAsJsonPrimitive("l").getAsFloat(),2);
        float previousPrice =  NumberRounder.roundFloat(response.getAsJsonPrimitive("pc").getAsFloat(),2);
        Calendar today = Calendar.getInstance();
        Stock stock = stockRepository.findBySymbol(symbol);
        /*
        LastPrice newLastPriceObj = LastPrice.builder()
                .stock(stock)
                .currentPrice(currentPrice)
                .highPrice(highPrice)
                .lowPrice(lowPrice)
                .openPrice(openPrice)
                .previousClosePrice(previousPrice)
                .timeOfRetrieval(today.getTime())
                .build();

         */
        if (lastPriceRepository.findByStock(stock) == null){
            LastPrice newLastPriceObj = LastPrice.builder()
                    .stock(stock)
                    .currentPrice(currentPrice)
                    .highPrice(highPrice)
                    .lowPrice(lowPrice)
                    .openPrice(openPrice)
                    .previousClosePrice(previousPrice)
                    .timeOfRetrieval(today.getTime())
                    .build();
            lastPriceRepository.save(newLastPriceObj);
        } else {
            LastPrice lastPrice = lastPriceRepository.findByStock(stock);
            System.out.println("lastPrice retreived: "+lastPrice.toString());

            lastPrice.setPreviousClosePrice(previousPrice);
            lastPrice.setLowPrice(lowPrice);
            lastPrice.setHighPrice(highPrice);
            lastPrice.setOpenPrice(openPrice);
            lastPrice.setCurrentPrice(currentPrice);
            lastPrice.setTimeOfRetrieval(today.getTime());
            lastPrice.setStock(stock);
            lastPriceRepository.save(lastPrice);
        }
    }
}

