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
import static org.springframework.web.bind.annotation.RequestMethod.*;

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
    @PostMapping("/placeoffer/{symbol}/{offerType}/{quantity}/{price}")
    public String placeOffer(@PathVariable("symbol") String symbol, @PathVariable("offerType") String offerType, @PathVariable("quantity") int quantity, @PathVariable("price") float price){
        boolean approvalQuantity = false;
        boolean approvalCash = false;
        double newOfferTotalValue = NumberRounder.roundDouble(price*quantity,2);
        UserAccount userAccount = userAccountRepository.findByNickName("Mr.T");
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
    @PostMapping("/replaceoffer/{id}/{symbol}/{offerType}/{quantity}/{price}")
    public String replaceOffer(@PathVariable("id") Long id, @PathVariable("symbol") String symbol, @PathVariable("offerType") String offerType, @PathVariable("quantity") int quantity, @PathVariable("price") float price){
        boolean approvalQuantity = false;
        boolean approvalCash = false;
        double newOfferTotalValue = NumberRounder.roundDouble(price*quantity, 2);
        UserAccount userAccount = userAccountRepository.findByNickName("Mr.T");
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
    @DeleteMapping("/deleteoffer/{id}")
    public String deleteOffer(@PathVariable("id") long id){
        UserAccount defaultUserAccount = userAccountRepository.findByNickName("Mr.T");
        List<Offer> userOffers = defaultUserAccount.getOffers();
        System.out.println("offer to be deleted: "+id);
        for (Offer offer: userOffers) {
            if (offer.getId() == id){
                userOffers.remove(offer);
                userAccountRepository.save(defaultUserAccount);
                //offerRepository.deleteById(id);
                return "Offer deleted!";
            }
        }
        return "Offer is NOT deleted!";
    }

    @GetMapping("getuseraccount")
    public UserAccount getUserAccount(){
        UserAccount defaultUserAccount = userAccountRepository.findByNickName("Mr.T");
        stockPerformanceListUpdater.getStockPerformanceList(defaultUserAccount);
        portfolioPerformanceUpdater.updatePortfolioPerformance(defaultUserAccount);
        userAccountRepository.save(defaultUserAccount);

        return userAccountRepository.findByNickName("Mr.T");
    }

    @GetMapping("getalloffers")
    public List<Offer> getAllOffers(){
        UserAccount defaultUserAccount = userAccountRepository.findByNickName("Mr.T");
        return defaultUserAccount.getOffers();
    }

    //USED
    @GetMapping("getoffers/{stock}")
    public List<Offer> getOffersPerStock(@PathVariable("stock") String stock){
        UserAccount defaultUserAccount = userAccountRepository.findByNickName("Mr.T");
        return defaultUserAccount.getOffers().stream().filter(offer -> offer.getStock().getSymbol().equals(stock)).collect(Collectors.toList());
    }

    //USED
    @GetMapping("getStockPerformanceList")
    public List<StockPerformance> getStockPerformanceList(){
        UserAccount defaultUserAccount = userAccountRepository.findByNickName("Mr.T");
        return stockPerformanceListUpdater.getStockPerformanceList(defaultUserAccount);

    }

    //USED
    @GetMapping("getStockPerformance/{stock}")
    public StockPerformance getStockPerformanceListPerStock(@PathVariable("stock") String stock_){
        UserAccount defaultUserAccount = userAccountRepository.findByNickName("Mr.T");
        Stock stock = stockRepository.findBySymbol(stock_);
        return stockPerformanceListUpdater.getStockPerformance(defaultUserAccount, stock);
    }

    //USED
    @GetMapping("getportfolioperformance")
    public PortfolioPerformance getPortfolioPerformance(){
        UserAccount defaultUserAccount = userAccountRepository.findByNickName("Mr.T");
        return portfolioPerformanceUpdater.updatePortfolioPerformance(defaultUserAccount);
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
    @GetMapping("getprofilecardinfo")
    public ProfileCardInfo getProfileCardInfo(){
        UserAccount defaultUserAccount = userAccountRepository.findByNickName("Mr.T");

        return ProfileCardInfo.builder()
                .username(defaultUserAccount.getUsername())
                .nickName(defaultUserAccount.getNickName())
                .profilePic(defaultUserAccount.getProfilePic_())
                .dateOfRegistration(defaultUserAccount.getDateOfRegistration())
                .build();
    }

    @GetMapping("/getcash")
    public double getCash(){
        UserAccount defaultUserAccount = userAccountRepository.findByNickName("Mr.T");
        return defaultUserAccount.getCash();
    }

    //USED
    @GetMapping("getStockDataForOffer/{stock}")
    public TradeSupportData getStockDataForOffer(@PathVariable("stock") String stock_){
        UserAccount defaultUserAccount = userAccountRepository.findByNickName("Mr.T");
        Stock stock = stockRepository.findBySymbol(stock_);

        return TradeSupportData.builder()
                .availableCash(portfolioAvailableCashForPurchaseProvider.providePortfolioAvailableCashForPurchase(defaultUserAccount))
                .stockQuantity(stockPerformanceListUpdater.getStockPerformance(defaultUserAccount, stock).getStockTotalAmount())
                .build();

    }
}

