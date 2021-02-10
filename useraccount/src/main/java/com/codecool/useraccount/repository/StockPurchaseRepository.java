package com.codecool.useraccount.repository;

import com.codecool.useraccount.model.Stock;
import com.codecool.useraccount.model.StockPurchase;
import com.codecool.useraccount.model.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StockPurchaseRepository extends JpaRepository<StockPurchase, Long> {
    List<StockPurchase> findAllByStockAndUserAccount(Stock stock, UserAccount userAccount);
}
