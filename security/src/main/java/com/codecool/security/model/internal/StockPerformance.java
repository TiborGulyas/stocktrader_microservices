package com.codecool.security.model.internal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StockPerformance {

    //@Id
    //@GeneratedValue(strategy = GenerationType.AUTO)
    //private Long id;
    //@ManyToOne
    private Stock stock = null;
    private int stockTotalAmount = 0;
    private double averagePurchasePrice = 0;
    private double totalPurchaseValue = 0;
    private double stockCurrentPrice = 0;
    private double stockCurrentValue = 0;
    private double stockValueChange = 0;

    //@JsonBackReference
    //@ToString.Exclude
    //@ManyToOne
    //private UserAccount userAccount;
}

