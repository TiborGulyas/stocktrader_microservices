package com.codecool.useraccount.service;


import com.codecool.useraccount.model.Offer;
import com.codecool.useraccount.model.OfferType;
import com.codecool.useraccount.model.UserAccount;
import org.springframework.stereotype.Component;


@Component
public class PortfolioAvailableCashForPurchaseProvider {
    public double providePortfolioAvailableCashForPurchase(UserAccount userAccount){
        double availableCash = userAccount.getCash();
        double cashInOffers = 0;
        if (userAccount.getOffers().size()>0){
            cashInOffers += userAccount.getOffers().stream().filter(offer -> offer.getOfferType() == OfferType.BUY).mapToDouble(Offer::getTotalValue).sum();
        }
        return availableCash-cashInOffers;
    }
}
