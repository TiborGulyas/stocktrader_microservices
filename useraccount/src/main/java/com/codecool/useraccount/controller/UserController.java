package com.codecool.useraccount.controller;

import com.codecool.useraccount.model.*;
import com.codecool.useraccount.model.internal.OfferList;
import com.codecool.useraccount.model.internal.PlaceOffer;
import com.codecool.useraccount.model.internal.ReplaceOffer;
import com.codecool.useraccount.model.internal.StockPerformanceList;
import com.codecool.useraccount.repository.OfferRepository;
import com.codecool.useraccount.repository.StockRepository;
import com.codecool.useraccount.repository.UserAccountRepository;
import com.codecool.useraccount.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
//@CrossOrigin(methods = {GET, POST, PUT, DELETE}, origins = "http://localhost:3000")
@RequestMapping("/user")
public class UserController {

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private OfferRepository offerRepository;

    @Autowired
    private OfferTypeProvider offerTypeProvider;

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


    //USED
    @PostMapping("/placeoffer")
    public String placeOffer(@RequestBody PlaceOffer placeOffer){
        boolean approvalQuantity = false;
        boolean approvalCash = false;
        float price = placeOffer.getPrice();
        int quantity = placeOffer.getQuantity();
        String symbol = placeOffer.getSymbol();
        String offerType = placeOffer.getOfferType();
        String username = placeOffer.getUserName();

        double newOfferTotalValue = NumberRounder.roundDouble(price*quantity,2);
        UserAccount userAccount = userAccountRepository.findByUsername(username);
        Stock stock = stockRepository.findBySymbol(symbol);
        OfferType offerTypeObj = offerTypeProvider.createOfferType(offerType);

        //in case of SELL: QUANTITY CHECK
        if (offerTypeObj == OfferType.SELL){
            int stockAvailable = portfolioAvailableStockQuantityProvider.providePortfolioStockQuantity(userAccount, stock);
            if ( stockAvailable >= quantity){
                approvalQuantity = true;
            } else {
                return "ERROR! Stock available: "+stockAvailable+" pcs; Stock requested to sell: "+quantity+" pcs.";
            }
            approvalCash = true;

        } else if (offerTypeObj == OfferType.BUY){
            double cashAvailable = portfolioAvailableCashForPurchaseProvider.providePortfolioAvailableCashForPurchase(userAccount);
            if (cashAvailable >= newOfferTotalValue){
                approvalCash = true;
            } else {
                return "ERROR! Cash available: $ "+cashAvailable+"; Stock value requested to buy: $ "+newOfferTotalValue+".";
            }
            approvalQuantity = true;
        }

        if (approvalQuantity && approvalCash){
            Offer offer = Offer.builder()
                    .offerDate(Calendar.getInstance().getTime())
                    .offerType(offerTypeObj)
                    .price(NumberRounder.roundFloat(price,2))
                    .quantity(quantity)
                    .totalValue(NumberRounder.roundDouble(quantity*price, 2))
                    .stock(stock)
                    .userAccount(userAccount)
                    .build();
            userAccount.getOffers().add(offer);
            userAccountRepository.save(userAccount);
            offerScanner.matchUserOffersPerUser(userAccountRepository.findByNickName("Mr.T"));
        }
        return "Offer Accepted!";
    }

    //USED
    @PostMapping("/replaceoffer")
    public String replaceOffer(@RequestBody ReplaceOffer replaceOffer){
        boolean approvalQuantity = false;
        boolean approvalCash = false;

        long id = replaceOffer.getOfferId();
        float price = replaceOffer.getPrice();
        int quantity = replaceOffer.getQuantity();
        String offerType = replaceOffer.getOfferType();
        String symbol = replaceOffer.getSymbol();
        String username = replaceOffer.getUserName();

        double newOfferTotalValue = NumberRounder.roundDouble(price*quantity, 2);
        UserAccount userAccount = userAccountRepository.findByUsername(username);
        OfferType newOfferType = offerTypeProvider.createOfferType(offerType);
        Stock stock = stockRepository.findBySymbol(symbol);
        Offer offer = offerRepository.getOne(id);
        double originalOfferTotalValue = offer.getTotalValue();
        double originalOfferQuantity = offer.getQuantity();

        //in case of SELL: QUANTITY CHECK
        if (newOfferType == OfferType.SELL){
            double stockAvailable = portfolioAvailableStockQuantityProvider.providePortfolioStockQuantity(userAccount, stock)+originalOfferQuantity;
            if (stockAvailable >= quantity){
                approvalQuantity = true;
            } else {
                return "ERROR! Stock available: "+stockAvailable+" pcs; Stock requested to sell: "+quantity+" pcs.";
            }
            approvalCash = true;
        } else if (newOfferType == OfferType.BUY){
            double cashAvailable = portfolioAvailableCashForPurchaseProvider.providePortfolioAvailableCashForPurchase(userAccount) + originalOfferTotalValue;
            if (cashAvailable >= newOfferTotalValue){
                approvalCash = true;
            } else {
                return "ERROR! Cash available: $ "+cashAvailable+"; Stock value requested to buy: $ "+newOfferTotalValue+".";
            }
            approvalQuantity = true;
        }

        if (approvalQuantity && approvalCash) {
            if (offer.getStock().getSymbol().equals(symbol)) {
                offer.setOfferType(newOfferType);
                offer.setPrice(NumberRounder.roundDouble(price, 2));
                offer.setQuantity(quantity);
                offer.setTotalValue(NumberRounder.roundDouble(quantity*price,2));
                offer.setOfferDate(Calendar.getInstance().getTime());
            }
            offerRepository.save(offer);
            offerScanner.matchUserOffersPerUser(userAccount);
            return "Offer Replaced!";
        }
        return "Offer is NOT replaced!";
    }

    //USED
    @DeleteMapping("/deleteoffer/{offerId}/{username}")
    public String deleteOffer(@PathVariable("offerId") Long offerId, @PathVariable("username") String username){
        System.out.println("DELETE OFFER TRIGGERED");

        UserAccount defaultUserAccount = userAccountRepository.findByUsername(username);
        List<Offer> userOffers = defaultUserAccount.getOffers();
        System.out.println("offer to be deleted: "+offerId);
        for (Offer offer: userOffers) {
            if (offer.getId() == offerId){
                userOffers.remove(offer);
                userAccountRepository.save(defaultUserAccount);
                //offerRepository.deleteById(id);
                return "Offer deleted!";
            }
        }
        return "Offer is NOT deleted!";
    }

    @GetMapping("getuseraccount/{username}")
    public UserAccount getUserAccount(@PathVariable("username") String username){
        UserAccount userAccount = userAccountRepository.findByUsername(username);
        //stockPerformanceListUpdater.getStockPerformanceList(userAccount);
        //portfolioPerformanceUpdater.updatePortfolioPerformance(userAccount);
        //userAccountRepository.save(userAccount);

        return userAccount;
    }

    @GetMapping("getalloffers/{username}")
    public OfferList getAllOffers(@PathVariable("username") String username){
        UserAccount userAccount = userAccountRepository.findByUsername(username);
        OfferList offerList = OfferList.builder()
                .offers(userAccount.getOffers())
                .build();
        return offerList;
    }

    //USED
    @GetMapping("getoffers/{username}/{stock}")
    public OfferList getOffersPerStock(@PathVariable("username") String username, @PathVariable("stock") String stock_){
        System.out.println("----GET OFFERS DATA:");
        System.out.println(username+", "+stock_);
        UserAccount userAccount = userAccountRepository.findByUsername(username);
        OfferList offerList = OfferList.builder()
                .offers(userAccount.getOffers().stream().filter(offer -> offer.getStock().getSymbol().equals(stock_)).collect(Collectors.toList()))
                .build();
        return offerList;
    }

    //USED
    @GetMapping("getStockPerformanceList/{username}")
    public StockPerformanceList getStockPerformanceList(@PathVariable("username") String username){
        UserAccount userAccount = userAccountRepository.findByUsername(username);
        StockPerformanceList stockPerformanceList = StockPerformanceList.builder()
                .stockPerformancesList(stockPerformanceListUpdater.getStockPerformanceList(userAccount))
                .build();
        return stockPerformanceList;
    }

    //USED
    @GetMapping("getstockperformance/{username}/{stock}")
    public StockPerformance getStockPerformanceListPerStock(@PathVariable("username") String username, @PathVariable("stock") String stock_){
        UserAccount userAccount = userAccountRepository.findByUsername(username);
        Stock stock = stockRepository.findBySymbol(stock_);
        return stockPerformanceListUpdater.getStockPerformance(userAccount, stock);
    }

    //USED
    @GetMapping("getportfolioperformance/{username}")
    public PortfolioPerformance getPortfolioPerformance(@PathVariable("username") String username){
        UserAccount userAccount = userAccountRepository.findByUsername(username);
        return portfolioPerformanceUpdater.updatePortfolioPerformance(userAccount);
        //userAccountRepository.save(defaultUserAccount);
        //return userAccountRepository.findByNickName("Mr.T").getPortfolioPerformance();
    }

    @GetMapping("getprofileinfo")
    public ProfileInformation getProfileInfo(){
        UserAccount defaultUserAccount = userAccountRepository.findByNickName("Mr.T");

        return ProfileInformation.builder()
                .profilePic(defaultUserAccount.getProfilePic_())
                .dateOfRegistration(defaultUserAccount.getDateOfRegistration())
                .id(defaultUserAccount.getId())
                .username(defaultUserAccount.getUsername())
                .nickName(defaultUserAccount.getNickName())
                .eMail(defaultUserAccount.getEMail_())
                .build();
    }

    //USED
    @GetMapping("getprofilecardinfo/{username}")
    public ProfileCardInfo getProfileCardInfo(@PathVariable("username") String username){
        UserAccount userAccount = userAccountRepository.findByUsername(username);

        return ProfileCardInfo.builder()
                .username(userAccount.getUsername())
                .nickName(userAccount.getNickName())
                .profilePic(userAccount.getProfilePic_())
                .dateOfRegistration(userAccount.getDateOfRegistration())
                .build();
    }

    @GetMapping("/getcash")
    public double getCash(){
        UserAccount defaultUserAccount = userAccountRepository.findByNickName("Mr.T");
        return defaultUserAccount.getCash();
    }

    //USED
    @GetMapping("getstockdataforoffer/{username}/{stock}")
    public TradeSupportData getStockDataForOffer(@PathVariable("username") String username, @PathVariable("stock") String stock_){
        UserAccount userAccount = userAccountRepository.findByUsername(username);
        Stock stock = stockRepository.findBySymbol(stock_);

        return TradeSupportData.builder()
                .availableCash(portfolioAvailableCashForPurchaseProvider.providePortfolioAvailableCashForPurchase(userAccount))
                .stockQuantity(stockPerformanceListUpdater.getStockPerformance(userAccount, stock).getStockTotalAmount())
                .build();

    }

    //USED
    @PostMapping("/registeruseraccount")
    public boolean registerUserAccount(@RequestBody UserAccountRegistration userAccountRegistration){
        System.out.println("----thisistheuser:------");
        System.out.println(userAccountRegistration);
        UserAccount userAccount = UserAccount.builder()
                .cash(NumberRounder.roundDouble(1000000,2))
                .cashInvested(NumberRounder.roundDouble(1000000,2))
                .username(userAccountRegistration.getUsername())
                .nickName(userAccountRegistration.getNickName())
                .dateOfRegistration(new Date())
                .profilePic_(userAccountRegistration.getProfilePic_())
                .eMail_(userAccountRegistration.getEMail_())
                .build();
        userAccountRepository.save(userAccount);
        return true;
    }
}

