package com.codecool.useraccount.service;

import com.codecool.useraccount.model.*;
import com.codecool.useraccount.repository.StockRepository;
import com.codecool.useraccount.repository.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.List;

@Component
public class TransactionServices {
    @Autowired
    UserAccountRepository userAccountRepository;

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private LastPriceProvider lastPriceProvider;


    public int getTotalQuantityofStocks(List<StockPurchase> stockPurchases){
        int totalQuantity = 0;
        for (StockPurchase stockPurchase:stockPurchases) {
            totalQuantity += stockPurchase.getQuantity();
        }
        return totalQuantity;
    }

    public double getTotalValueOfOffer(Offer offer){
        return  NumberRounder.roundDouble(offer.getQuantity()*offer.getPrice(),2);
    }

    public void excecutePurchaseOffer(Offer offer){
        Stock offerStock = offer.getStock();
        LastPrice lastPrice = lastPriceProvider.getLastPrice(offerStock);
        UserAccount userAccount = offer.getUserAccount();
        double userCash = userAccount.getCash();
        double transactionValue = lastPrice.getCurrentPrice()*offer.getQuantity();
        StockPurchase stockPurchase = StockPurchase.builder()
                .purchasePrice(NumberRounder.roundDouble(lastPrice.getCurrentPrice(),2))
                .purchaseDate(lastPrice.getTimeOfRetrieval())
                .stock(offerStock)
                .quantity(offer.getQuantity())
                .userAccount(userAccount)
                .build();
        userAccount.getPortfolio().add(stockPurchase);
        userAccount.setCash(NumberRounder.roundDouble(userCash-transactionValue,2));
        userAccount.getOffers().remove(offer);
        userAccountRepository.save(userAccount);
    }

    public void excecuteSalesOffer(Offer offer){
        Stock offerStock = offer.getStock();
        LastPrice lastPrice = lastPriceProvider.getLastPrice(offerStock);
        UserAccount userAccount = offer.getUserAccount();
        List<StockPurchase> stockPurchases = userAccount.getPortfolio();
        int offerQuantity = offer.getQuantity();
        double  transactionValue = lastPrice.getCurrentPrice()*offerQuantity;

        Iterator<StockPurchase> stockPurchaseIterator = stockPurchases.listIterator();
        while (stockPurchaseIterator.hasNext()) {
            StockPurchase stockPurchase = stockPurchaseIterator.next();
            if (stockPurchase.getStock() == offer.getStock()) {
                if (stockPurchase.getQuantity() <= offerQuantity) {
                    offerQuantity -= stockPurchase.getQuantity();
                    stockPurchaseIterator.remove();
                    if (offerQuantity == 0) {
                        break;
                    }
                } else if (stockPurchase.getQuantity() > offerQuantity) {
                    stockPurchase.setQuantity(stockPurchase.getQuantity() - offerQuantity);
                    offerQuantity = 0;
                    break;
                }
            }
        }
        if (offerQuantity == 0){
            userAccount.setPortfolio(stockPurchases);
            userAccount.getOffers().remove(offer);
            userAccount.setCash(NumberRounder.roundDouble(userAccount.getCash()+transactionValue,2));
            userAccountRepository.save(userAccount);
        }
    }
}
