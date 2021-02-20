package com.codecool.useraccount.controller;

import com.codecool.useraccount.model.*;
import com.codecool.useraccount.repository.OfferRepository;
import com.codecool.useraccount.repository.StockRepository;
import com.codecool.useraccount.repository.UserAccountRepository;
import com.codecool.useraccount.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
//@CrossOrigin
@RequestMapping("/offer")
public class MatchAllOffersController {

    @Autowired
    OfferScanner offerScanner;

    @Autowired
    StockPerformanceListUpdater stockPerformanceListUpdater;

    @Autowired
    PortfolioPerformanceUpdater portfolioPerformanceUpdater;

    @Autowired
    PortfolioAvailableStockQuantityProvider portfolioAvailableStockQuantityProvider;

    @Autowired
    PortfolioAvailableCashForPurchaseProvider portfolioAvailableCashForPurchaseProvider;



    @PostMapping("/matchall")
    public void placeOffer() {
        System.out.println("!!!!!!MATCHALL TRIGGERED!!!!!");
        offerScanner.matchUserOffers();
    }
}

