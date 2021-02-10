package com.codecool.useraccount.service_caller;

import com.codecool.useraccount.model.LastPrice;
import com.codecool.useraccount.model.Stock;
import com.codecool.useraccount.model.StockList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Objects;

@Service
public class StockTraderServiceCaller {

    @Autowired
    private RestTemplate template;

    private final String basicURL = "http://stockpricerepo/stock/";

    public StockList getAllStocks(){
        return template.getForObject(basicURL + "getallstocks/", StockList.class);
    }

    public LastPrice getLastPrice(Stock stock){
        String lastPriceURL = UriComponentsBuilder.fromHttpUrl(basicURL + "getquote/")
                .path(stock.getSymbol()).toUriString();
        return template.getForEntity(lastPriceURL, LastPrice.class).getBody();
    }

}
