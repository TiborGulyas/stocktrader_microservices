package com.codecool.security.model.internal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PortfolioPerformance {
    //@Id
    //@GeneratedValue(strategy = GenerationType.AUTO)
    //private Long id;

    private double investedCash;
    private double portfolioTotalValue;
    private double portfolioTotalStockValue;
    private double portfolioFreeCashValue;
    private double portfolioTotalStockPurchaseValue;
    private double percentageStockValue;
    private double percentageCashValue;
    private double currentStockProfit;
    private double investedCashProfit;
    private double percentageCurrentStockProfit;
    private double percentageInvestedCashProfit;

}

