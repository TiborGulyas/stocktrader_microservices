package com.codecool.security.controller;


import com.codecool.security.model.PlaceOffer;
import com.codecool.security.service_caller.UserAccountCaller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

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

}
