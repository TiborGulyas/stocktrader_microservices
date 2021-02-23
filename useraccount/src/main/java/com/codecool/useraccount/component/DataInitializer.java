package com.codecool.useraccount.component;


import com.codecool.useraccount.model.*;
import com.codecool.useraccount.service.*;
import com.codecool.useraccount.repository.StockRepository;
import com.codecool.useraccount.repository.UserAccountRepository;


import com.codecool.useraccount.service_caller.StockTraderServiceCaller;
import com.google.gson.JsonObject;
import nonapi.io.github.classgraph.json.JSONUtils;
import org.apache.http.impl.BHttpConnectionBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Stream;

@Component
public class DataInitializer {


    @Autowired
    UserAccountRepository userAccountRepository;

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private OfferTypeProvider offerTypeProvider;

    @Autowired
    private StockTraderServiceCaller stockTraderServiceCaller;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private PingHost pingHost;


    public void initData() throws IOException, ParseException {
        System.out.println("init persistance");
        ArrayList<String> symbols = new ArrayList<>(
                Arrays.asList("GOOGL", "AAPL", "TSLA", "GME", "AMC"));


        // PERSIST ALL STOCKS
        StockList stocks = stockTraderServiceCaller.getAllStocks();

        stocks.getStocks().forEach(stock ->
                stockRepository.save(stock));


        stocks.getStocks().forEach(stock ->
                stockRepository.save(stock));


        //SAVE TRADER
        int port = 8071;
        String apicall = "http://userservice/registeruser";
        Trader testTrader = Trader.builder()
                .password("helloworld")
                .username("gulyastibor")
                .build();
        if (pingHost.pingHost("localhost", port, 100)){
            restTemplate.postForLocation(apicall, testTrader);
        }

        // INIT DEFAULT ACCOUNT
        UserAccount userAccount = UserAccount.builder()
                .cash(NumberRounder.roundDouble(1000000,2))
                .cashInvested(NumberRounder.roundDouble(1000000,2))
                .username("Tibor Gulyás")
                .nickName("Mr.T")
                .dateOfRegistration(new Date())
                .profilePic_("data:image/jpeg;base64,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")
                .eMail_("gulyastibor@gmail.com")
                .build();



        // ADD STOCK PURCHASE TO ACCOUNT -> SAVE ACCOUNT
        Stock savedAAPL = stockRepository.findBySymbol("AAPL");
        StockPurchase stockPurchase = StockPurchase.builder()
                .purchaseDate(Calendar.getInstance().getTime())
                .stock(savedAAPL)
                .purchasePrice(NumberRounder.roundDouble(150.23,2))
                .quantity(1000)
                .userAccount(userAccount)
                .build();
        userAccount.getPortfolio().add(stockPurchase);

        StockPurchase stockPurchase2 = StockPurchase.builder()
                .purchaseDate(Calendar.getInstance().getTime())
                .stock(savedAAPL)
                .purchasePrice(NumberRounder.roundDouble(170.23,2))
                .quantity(200)
                .userAccount(userAccount)
                .build();
        userAccount.getPortfolio().add(stockPurchase2);


        Stock savedTSLA = stockRepository.findBySymbol("TSLA");
        StockPurchase stockPurchase3 = StockPurchase.builder()
                .purchaseDate(Calendar.getInstance().getTime())
                .stock(savedTSLA)
                .purchasePrice(NumberRounder.roundDouble(250.44,2))
                .quantity(30)
                .userAccount(userAccount)
                .build();
        userAccount.getPortfolio().add(stockPurchase3);

        //System.out.println(userAccount.toString());
        userAccountRepository.save(userAccount);



        // RECALL SAVED ACCOUNT FROM DB -> ADD OFFERS TO ACCOUNT -> SAVE ACCOUNT
        UserAccount savedUserAccount2 = userAccountRepository.findByNickName("Mr.T");
        Stock stockApple = stockRepository.findBySymbol("AAPL");
        Stock stockTesla = stockRepository.findBySymbol("TSLA");

        Offer offerAAPL1 = Offer.builder()
                .offerDate(Calendar.getInstance().getTime())
                .offerType(offerTypeProvider.createOfferType("BUY"))
                .price(NumberRounder.roundDouble(170.2,2))
                .quantity(42)
                .totalValue(NumberRounder.roundDouble(170.2*42,2))
                .stock(stockApple)
                .userAccount(savedUserAccount2)
                .build();
        savedUserAccount2.getOffers().add(offerAAPL1);

        Offer offerAAPL2 = Offer.builder()
                .offerDate(Calendar.getInstance().getTime())
                .offerType(offerTypeProvider.createOfferType("BUY"))
                .price(NumberRounder.roundDouble(70.2,2))
                .quantity(102)
                .totalValue(NumberRounder.roundDouble(70.2*102,2))
                .stock(stockApple)
                .userAccount(savedUserAccount2)
                .build();
        savedUserAccount2.getOffers().add(offerAAPL2);


        Offer offerTSLA1 = Offer.builder()
                .offerDate(Calendar.getInstance().getTime())
                .offerType(offerTypeProvider.createOfferType("BUY"))
                .price(NumberRounder.roundDouble(510.2,2))
                .quantity(15)
                .totalValue(510.2*15)
                .stock(stockTesla)
                .userAccount(savedUserAccount2)
                .build();
        savedUserAccount2.getOffers().add(offerTSLA1);



        userAccountRepository.save(savedUserAccount2);

    }
}
