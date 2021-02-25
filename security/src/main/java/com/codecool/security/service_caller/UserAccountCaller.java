package com.codecool.security.service_caller;

import com.codecool.security.model.internal.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class UserAccountCaller {

    @Autowired
    private RestTemplate template;

    private final String userAccountURL = "http://useraccount/user/";


    public String placeOffer(PlaceOffer placeOffer){

        return template.postForObject(userAccountURL+"placeoffer", placeOffer, String.class);
    }

    public String replaceOffer(ReplaceOffer replaceOffer) {
        return template.postForObject(userAccountURL+"replaceoffer", replaceOffer, String.class);
    }

    public String deleteOffer(DeleteOffer deleteOffer) {
        template.delete(userAccountURL+"deleteoffer/"+deleteOffer.getOfferId()+"/"+deleteOffer.getUserName());
        return "Offer deleted!";
    }

    public UserAccount getuseraccount(String username) {
        return template.getForObject(userAccountURL+"getuseraccount/"+username, UserAccount.class);
    }

    public OfferList getAllOffers(String username) {
        return template.getForObject(userAccountURL+"getalloffers/"+username, OfferList.class);
    }

    public OfferList getOffersPerStock(String username, String stock) {
        return template.getForObject(userAccountURL+"getoffers/"+username+"/"+stock, OfferList.class);
    }

    public StockPerformanceList getStockPerformanceList(String username) {
        return template.getForObject(userAccountURL+"getStockPerformanceList/"+username, StockPerformanceList.class);
    }

}
