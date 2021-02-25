package com.codecool.security.controller;


import com.codecool.security.model.internal.*;
import com.codecool.security.service_caller.UserAccountCaller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/auth/user")
public class UserAccountController {

    @Autowired
    UserAccountCaller userAccountCaller;

    @PostMapping("/placeoffer/{symbol}/{offerType}/{quantity}/{price}")
    public String placeOffer(@PathVariable("symbol") String symbol, @PathVariable("offerType") String offerType, @PathVariable("quantity") int quantity, @PathVariable("price") float price){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String token_username = (String) authentication.getPrincipal();

        PlaceOffer placeOffer = PlaceOffer.builder()
                .symbol(symbol)
                .offerType(offerType)
                .quantity(quantity)
                .price(price)
                .userName(token_username)
                .build();

        return userAccountCaller.placeOffer(placeOffer);

    }

    @PostMapping("/replaceoffer/{id}/{symbol}/{offerType}/{quantity}/{price}")
    public String replaceOffer(@PathVariable("id") Long id, @PathVariable("symbol") String symbol, @PathVariable("offerType") String offerType, @PathVariable("quantity") int quantity, @PathVariable("price") float price){
        System.out.println("REPLACEOFFER TRIGGERED");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String token_username = (String) authentication.getPrincipal();

        ReplaceOffer replaceOffer = ReplaceOffer.builder()
                .offerId(id)
                .symbol(symbol)
                .offerType(offerType)
                .quantity(quantity)
                .price(price)
                .userName(token_username)
                .build();

        return userAccountCaller.replaceOffer(replaceOffer);
    }

    @DeleteMapping("/deleteoffer/{id}")
    public String deleteOffer(@PathVariable("id") Long id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String token_username = (String) authentication.getPrincipal();

        DeleteOffer deleteOffer = DeleteOffer.builder()
                .offerId(id)
                .userName(token_username)
                .build();

        return userAccountCaller.deleteOffer(deleteOffer);
    }

    @GetMapping("/getuseraccount")
    public UserAccount getUserAccount(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String token_username = (String) authentication.getPrincipal();

        return userAccountCaller.getuseraccount(token_username);
    }

    @GetMapping("/getalloffers")
    public OfferList getAllOffers(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String token_username = (String) authentication.getPrincipal();

        return userAccountCaller.getAllOffers(token_username);
    }

    @GetMapping("/getoffers/{stock}")
    public OfferList getOffersPerStock(@PathVariable("stock") String stock_){
        System.out.println("!!!GETOFFERS STOCk string: ");
        System.out.println(stock_);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String token_username = (String) authentication.getPrincipal();

        return userAccountCaller.getOffersPerStock(token_username, stock_);
    }

    @GetMapping("getstockperformancelist")
    public StockPerformanceList getStockPerformanceList(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String token_username = (String) authentication.getPrincipal();

        return userAccountCaller.getStockPerformanceList(token_username);
    }

    @GetMapping("getstockperformance/{stock}")
    public StockPerformance getStockPerformance(@PathVariable("stock") String stock){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String token_username = (String) authentication.getPrincipal();

        return userAccountCaller.getStockPerformance(token_username, stock);
    }

    @GetMapping("getportfolioperformance")
    public PortfolioPerformance getPortfolioPerformance(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String token_username = (String) authentication.getPrincipal();

        return userAccountCaller.getPortfolioPerformance(token_username);
    }

    @GetMapping("getprofilecardinfo")
    public ProfileCardInfo getProfileCardInfo(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String token_username = (String) authentication.getPrincipal();

        return userAccountCaller.getProfileCardInfo(token_username);
    }

    @GetMapping("getstockdataforoffer/{stock}")
    public TradeSupportData getStockDataForOffer(@PathVariable("stock") String stock){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String token_username = (String) authentication.getPrincipal();

        return userAccountCaller.getStockDataForOffer(token_username, stock);
    }
}
