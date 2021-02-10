package com.codecool.useraccount.repository;

import com.codecool.useraccount.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StockRepository extends JpaRepository<Stock, Long> {
    Stock findBySymbol(String symbol);
}

