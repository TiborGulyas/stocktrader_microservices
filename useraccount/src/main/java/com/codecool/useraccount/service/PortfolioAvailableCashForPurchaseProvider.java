package com.codecool.useraccount.service;


import com.codecool.useraccount.model.Offer;
import com.codecool.useraccount.model.UserAccount;
import org.springframework.stereotype.Component;


@Component
public class PortfolioAvailableCashForPurchaseProvider {
    public double providePortfolioAvailableCashForPurchase(UserAccount userAccount){
        double availableCash = userAccount.getCash();
        double cashInOffers = userAccount.getOffers().stream().mapToDouble(Offer::getTotalValue).sum();
        return availableCash-cashInOffers;
    }
}
