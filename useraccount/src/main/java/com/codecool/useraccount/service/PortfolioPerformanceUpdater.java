package com.codecool.useraccount.service;

import com.codecool.useraccount.model.*;
import com.codecool.useraccount.repository.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class PortfolioPerformanceUpdater {

    @Autowired
    private LastPriceProvider lastPriceProvider;

    @Autowired
    UserAccountRepository userAccountRepository;

    public PortfolioPerformance updatePortfolioPerformance(UserAccount userAccount){
        //PortfolioPerformance portfolioPerformance = userAccount.getPortfolioPerformance();
        //System.out.println("this is the portfolio performance: "+portfolioPerformance);
        PortfolioPerformance portfolioPerformance = new PortfolioPerformance();
        List<StockPurchase> portfolio = userAccount.getPortfolio();
        HashMap<String, LastPrice> lastPricesRequested = new HashMap<>();

        //MAKE DB CALL ONLY IF NEW LAST PRICE APPEARS
        portfolio.forEach(
                stockPurchase -> {
                    if (!lastPricesRequested.containsKey(stockPurchase.getStock().getSymbol())) {
                        lastPricesRequested.put(stockPurchase.getStock().getSymbol(), lastPriceProvider.getLastPrice(stockPurchase.getStock()));
                    }
                });

        System.out.println("STOCKS OF STOCKPURCHASE:");
        System.out.println(lastPricesRequested);

        portfolioPerformance.setPortfolioTotalStockValue(NumberRounder.roundDouble(portfolio.stream()
                .mapToDouble(stockPurchase ->
                        stockPurchase.getQuantity() * lastPricesRequested.get(stockPurchase.getStock().getSymbol()).getCurrentPrice()).sum(), 2)
        );

        portfolioPerformance.setCurrentStockProfit(NumberRounder.roundDouble(portfolio.stream().mapToDouble(
                stockPurchase -> (lastPricesRequested.get(stockPurchase.getStock().getSymbol()).getCurrentPrice() - stockPurchase.getPurchasePrice()) * stockPurchase.getQuantity()).sum(), 2)
        );

        portfolioPerformance.setPortfolioTotalStockPurchaseValue(NumberRounder.roundDouble(portfolio.stream().mapToDouble(
                stockPurchase -> stockPurchase.getQuantity()*stockPurchase.getPurchasePrice()).sum(), 2)
        );

        portfolioPerformance.setPortfolioTotalValue(NumberRounder.roundDouble(userAccount.getCash()+portfolioPerformance.getPortfolioTotalStockValue(),2));
        portfolioPerformance.setPercentageStockValue(NumberRounder.roundDouble(portfolioPerformance.getPortfolioTotalStockValue()/portfolioPerformance.getPortfolioTotalValue()*100, 2));
        portfolioPerformance.setPercentageCashValue(NumberRounder.roundDouble(userAccount.getCash()/portfolioPerformance.getPortfolioTotalValue()*100, 2));
        portfolioPerformance.setInvestedCashProfit(NumberRounder.roundDouble(portfolioPerformance.getPortfolioTotalValue()-userAccount.getCashInvested(),2));
        portfolioPerformance.setPercentageCurrentStockProfit(NumberRounder.roundDouble(portfolioPerformance.getCurrentStockProfit()/portfolioPerformance.getPortfolioTotalStockPurchaseValue()*100,2));
        portfolioPerformance.setPercentageInvestedCashProfit(NumberRounder.roundDouble((portfolioPerformance.getPortfolioTotalValue()/userAccount.getCashInvested()-1)*100,2));
        portfolioPerformance.setPortfolioFreeCashValue(NumberRounder.roundDouble(userAccount.getCash(),2));
        portfolioPerformance.setInvestedCash(NumberRounder.roundDouble(userAccount.getCashInvested(),2));

        return  portfolioPerformance;

        /*
        for (StockPurchase stockPurchase:portfolio) {
            double currentPrice = lastPriceRepository.findByStock(stockPurchase.getStock()).getCurrentPrice();
            portfolioPerformance.setPortfolioTotalStockValue(portfolioPerformance.getPortfolioTotalStockValue()+currentPrice*stockPurchase.getQuantity());
            portfolioPerformance.setCurrentStockProfit(portfolioPerformance.getCurrentStockProfit()+(currentPrice-stockPurchase.getPurchasePrice())*stockPurchase.getQuantity());
        }


        portfolioPerformance.setPortfolioTotalValue(userAccount.getCash()+portfolioPerformance.getPortfolioTotalStockValue());
        percentageStockValue = totalStockValue/totalValue*100;
        percentageCashValue = userAccount.getCash()/totalValue*100;
        investedCashProfit = totalValue - userAccount.getCashInvested();
        percentageCurrentStockProfit = currentStockProfit/totalStockValue*100;
        percentageInvestedCashProfit = (totalValue/userAccount.getCashInvested()-1)*100;

        portfolioPerformance.setPortfolioTotalValue(NumberRounder.roundDouble(totalValue,2));
        portfolioPerformance.setPortfolioTotalStockValue(NumberRounder.roundDouble(totalStockValue,2));
        portfolioPerformance.setPercentageCashValue(NumberRounder.roundDouble(percentageCashValue,2));
        portfolioPerformance.setPercentageStockValue(NumberRounder.roundDouble(percentageStockValue,2));
        portfolioPerformance.setCurrentStockProfit(NumberRounder.roundDouble(currentStockProfit,2));
        portfolioPerformance.setInvestedCashProfit(NumberRounder.roundDouble(investedCashProfit,2));
        portfolioPerformance.setPercentageCurrentStockProfit(NumberRounder.roundDouble(percentageCurrentStockProfit,2));
        portfolioPerformance.setPercentageInvestedCashProfit(NumberRounder.roundDouble(percentageInvestedCashProfit,2));
        //userAccountRepository.save(userAccount);
        */
    }
}
