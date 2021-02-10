package com.codecool.stocktrader.service;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class StockLogoProvider {
    public String provideStockLogo(String stock){
        return "https://logo.clearbit.com/"+stock+"?size=100";
    }
}

