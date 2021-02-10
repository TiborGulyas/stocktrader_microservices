package com.codecool.useraccount.service;

import com.codecool.useraccount.model.LastPrice;
import com.codecool.useraccount.model.Stock;
import com.codecool.useraccount.service_caller.StockTraderServiceCaller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LastPriceProvider {

    @Autowired
    private StockTraderServiceCaller stockTraderServiceCaller;

    public LastPrice getLastPrice(Stock stock){
        return stockTraderServiceCaller.getLastPrice(stock);
    }
}
